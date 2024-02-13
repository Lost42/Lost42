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
import org.springframework.util.StringUtils;
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
            "/swagger", "/swagger-ui/**", "/v3/api-docs/**",
            "/oauth2/**"
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
        if (!StringUtils.hasText(accessToken) || !StringUtils.hasText(refreshToken)) {
            filterChain.doFilter(request,response);
            return;
        }

        // AccessToken, RefreshToken 시나리오
        // (0) AT, RT 없을 경우 -> 예외 처리
        // (1) AT 만료X (RT 존재 및 유효 유무 판단만 하고 만료 판단은 하지 않는다)
        // (1-1) RT가 존재하고 서명이 맞을 경우 : 인증된 것으로 판정, (1-2) RT가 존재하지만 서명이 틀린 경우 : RT 서명 오류 예외
        // (2) AT 만료O (RT 존재 및 유효 유무와 만료되었는지 여부 판단)
        // (2-1) RT 존재O, 유효O, 만료X -> AT 재발급
        // (2-2) RT 존재O, 유효O, 만료O -> 로그인 요청

//        if (!StringUtils.hasText(accessToken)) {
//            handleAuthErrorException(response, AuthErrorCode.EMPTY_ACCESS_TOKEN);
//            return;
//        } else if (!StringUtils.hasText(refreshToken)) {
//            handleAuthErrorException(response, AuthErrorCode.EMPTY_REFRESH_TOKEN);
//            return;
//        }

        // AT 만료 체크
        boolean isExpireAccess = jwtTokenUtil.isTokenExpiredWithAccessToken(accessToken);
        boolean isValidRefresh = jwtTokenUtil.validateRefreshToken(refreshToken);
        boolean isForbiddenRefresh = forbiddenTokenService.isExist(refreshToken);
        log.warn("isForbiddenRefresh: {}", isForbiddenRefresh);

        if (isExpireAccess) {
            if (!isForbiddenRefresh || isValidRefresh) {
                boolean isExpireRefresh = jwtTokenUtil.isTokenExpiredWithRefreshToken(refreshToken);
                if (isExpireRefresh) {
                    handleAuthErrorException(response, AuthErrorCode.NEED_LOGIN);
                } else {
                    CustomUserDetails userDetails = (CustomUserDetails) getUserDetails(accessToken);
                    String newAccessToken = tokenProvider.generateAccessToken(JwtTokenInfo.fromCustomUserDetails(userDetails));
                    response.addHeader("Authorization", "Bearer " + newAccessToken);
                    return;
                }
            } else {
                handleAuthErrorException(response, AuthErrorCode.FAILED_AUTHENTICATION);
            }
        }

        CustomUserDetails securityUser = (CustomUserDetails) getUserDetails(accessToken);
        if (securityUser == null) {
            handleAuthErrorException(response, AuthErrorCode.FAILED_AUTHENTICATION);
            return;
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
