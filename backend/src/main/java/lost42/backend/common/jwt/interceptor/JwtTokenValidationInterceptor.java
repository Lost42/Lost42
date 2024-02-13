package lost42.backend.common.jwt.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.auth.exception.AuthErrorCode;
import lost42.backend.common.jwt.JwtTokenValidation;
import lost42.backend.common.response.ErrorResponse;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtTokenValidationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            JwtTokenValidation annotation = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), JwtTokenValidation.class);
            if (annotation != null && annotation.enable()) {
                log.warn("JwtInterceptor: {}", request.getRequestURI());
                String accessToken = resolveAccessToken(request);
                String refreshToken = resolveRefreshToken(request);

                if (accessToken == null) {
                    handleAuthErrorException(response, AuthErrorCode.EMPTY_ACCESS_TOKEN);
                    return false;
                } else if (refreshToken == null) {
                    handleAuthErrorException(response, AuthErrorCode.EMPTY_REFRESH_TOKEN);
                    return false;
                }
            }
        }
        return true;
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName()))
                    return cookie.getValue();
            }
        }
        return null;
    }

    private void handleAuthErrorException(HttpServletResponse response, AuthErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = new ObjectMapper().writeValueAsString(ErrorResponse.of(errorCode.getMessage()));
        response.getWriter().write(json);
    }
}

