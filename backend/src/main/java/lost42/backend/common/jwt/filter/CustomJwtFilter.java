package lost42.backend.common.jwt.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.Response.ErrorResponse;
import lost42.backend.common.jwt.JwtTokenUtil;
import lost42.backend.common.jwt.dto.JwtTokenInfo;
import lost42.backend.common.jwt.provider.TokenProvider;
import lost42.backend.common.redis.forbidden.ForbiddenTokenService;
import lost42.backend.common.redis.refreshtoken.RefreshTokenService;
import lost42.backend.common.auth.dto.CustomUserDetails;
import lost42.backend.common.auth.exception.AuthErrorCode;
import lost42.backend.common.auth.exception.AuthErrorException;
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
            "/swagger", "/swagger-ui/**", "/v3/api-docs/**",
            "/api/v1/members/signin", "/oauth2/**", "/api/v1/members/signup",
            "/api/v1/jwt/test",
            "/api/v1/members/find-email", "/api/v1/members/find-password"
    );


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (ignoreTokenRequest(request)) {
            log.info("JWT Filer: Ignoring request: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Request: {}", request);
        String accessToken = jwtTokenUtil.resolveAccessToken(request);
        String refreshToken = jwtTokenUtil.resolveRefreshToken(request);

        if (accessToken != null) {
            if (!forbiddenTokenService.isExist(refreshToken)) {
                handleAuthErrorException(response, AuthErrorCode.INVALID_REFRESH_TOKEN);
                return;
            }
            CustomUserDetails securityUser = (CustomUserDetails) getUserDetails(accessToken);
            if (securityUser == null) {
                handleAuthErrorException(response, AuthErrorCode.USER_NOT_FOUND);
                return;
            }
            if (jwtTokenUtil.validateAccessToken(accessToken, securityUser)) {
                setAuthenticationUser(securityUser, request);
            } else {
                boolean isValid = jwtTokenUtil.validateRefreshToken(refreshToken, securityUser);
                boolean isExist = refreshTokenService.isValidRefreshToken(refreshToken);
                if (isValid && isExist) {
                    JwtTokenInfo tokenInfo = JwtTokenInfo.fromCustomUserDetails(securityUser);
                    String newAccessToken = tokenProvider.generateAccessToken(tokenInfo);
                    response.setHeader("Authorization", "Bearer " + newAccessToken);
                    setAuthenticationUser(securityUser, request);
                } else if (!isValid) {
                    handleAuthErrorException(response, AuthErrorCode.USER_NOT_FOUND);
                    return;
                } else if (!isExist) {
                    handleAuthErrorException(response, AuthErrorCode.INVALID_REFRESH_TOKEN);
                    return;
                }
            }
        } else {
            handleAuthErrorException(response, AuthErrorCode.EMPTY_ACCESS_TOKEN);
            return;
        }

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
