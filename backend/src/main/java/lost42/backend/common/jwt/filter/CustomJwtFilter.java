package lost42.backend.common.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.response.ErrorResponse;
import lost42.backend.common.jwt.JwtTokenUtil;
import lost42.backend.common.jwt.dto.JwtTokenInfo;
import lost42.backend.common.jwt.provider.TokenProvider;
import lost42.backend.common.redis.forbidden.ForbiddenTokenService;
import lost42.backend.common.redis.refreshtoken.RefreshTokenService;
import lost42.backend.common.auth.dto.CustomUserDetails;
import lost42.backend.common.auth.exception.AuthErrorCode;
import lost42.backend.common.auth.service.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomJwtFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;

    private final List<String> jwtIgnoreUrl = List.of(
            "/", "/favicon.ico",
            "/swagger", "/swagger-ui/**", "/v3/api-docs/**"
//            "/api/v1/members/signin", "/oauth2/**", "/api/v1/members/signup",
//            "/api/v1/jwt/test",
//            "/api/v1/members/find-email", "/api/v1/members/find-password", "/api/v1/members/auth",
//            "/api/v1/boards/get"
    );


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (ignoreTokenRequest(request)) {
            log.info("JWT Filter: Ignoring request: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtTokenUtil.resolveAccessToken(request);
        String refreshToken = jwtTokenUtil.resolveRefreshToken(request);
        if (accessToken == null && refreshToken == null) {
            filterChain.doFilter(request,response);
            return;
        }

        // accessToken, refreshToken 시나리오
        // 1. accessToken 유효, refreshToken 유효 -> 그대로 로직 진행
        // 2. accessToken 유효, refreshToken 만료 -> 새로운 refreshToken 발급한 후에 로직 그대로 진행
        // 3. accessToken 만료, refreshToken 유효 -> 새로운 accessToken 발급
        // 4. accessToken 만료, refreshToken 만료 -> 재 로그인 필요

        // 사용이 금지된 refreshToken 이거나 존재하지 않는 refreshToken 일 경우
        if (!refreshTokenService.isExistRefreshToken(refreshToken) || forbiddenTokenService.isExist(refreshToken)) {
            handleAuthErrorException(response, AuthErrorCode.INVALID_REFRESH_TOKEN);
            return;
        }

        // 존재하지 않는 user 라면 (accessToken 조작)
        CustomUserDetails securityUser = (CustomUserDetails) getUserDetails(accessToken);
        if (securityUser == null) {
            handleAuthErrorException(response, AuthErrorCode.FAILED_AUTHENTICATION);
            return;
        }

        // 정상적인 토큰인가 (멤버 id비교)?
        boolean validAccess = jwtTokenUtil.validateAccessToken(accessToken, securityUser);
        boolean validRefresh = jwtTokenUtil.validateRefreshToken(refreshToken, securityUser);
        if (!validAccess || !validRefresh) {
            handleAuthErrorException(response, AuthErrorCode.FAILED_AUTHENTICATION);
            return;
        }

        // 만료된 토큰인가?
        boolean isExpireAccess = jwtTokenUtil.isTokenExpiredWithAccessToken(accessToken);
        boolean isExpireRefresh = jwtTokenUtil.isTokenExpiredWithRefreshToken(refreshToken);
        if (isExpireAccess) {
            if (!isExpireRefresh) {
                String newAccessToken = tokenProvider.generateAccessToken(JwtTokenInfo.fromCustomUserDetails(securityUser));

                response.setHeader("Authorization", "Bearer " + newAccessToken);
            } else {
                handleAuthErrorException(response, AuthErrorCode.NEED_LOGIN);
                return;
            }
        }

        setAuthenticationUser(securityUser, request);

        filterChain.doFilter(request, response);
    }

    private boolean ignoreTokenRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        Optional<String> judge = jwtIgnoreUrl.stream()
                .filter(v ->
                        Pattern.matches(v.replace("**", ".*"), uri) ||
                                Pattern.matches(v.replace("/**", ""), uri)

                )
                .findFirst();
        return judge.isPresent() || "OPTIONS".equals(method);
    }

    private UserDetails getUserDetails(String accessToken) {
        Long memberId = tokenProvider.getIdWithAccessToken(accessToken);
        return customUserDetailsService.loadUserByMemberId(memberId);
    }

    private void setAuthenticationUser(CustomUserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Authenticated user: {}", userDetails.getUsername());
    }

    /**
     * JwtFilter 내에서 에러 발생 시 에러 응답 반환
     */
    private void handleAuthErrorException(HttpServletResponse response, AuthErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        log.warn("JwtFilter Error: {}", errorCode.getMessage());

        String json = new ObjectMapper().writeValueAsString(ErrorResponse.of(errorCode.getMessage()));
        response.getWriter().write(json);
    }
}
