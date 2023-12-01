package lost42.backend.config.auth.hadler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.config.auth.MemberRole;
import lost42.backend.config.jwt.provider.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String accessToken = tokenProvider.generateAccessToken(authentication);

        // TODO Refresh 토큰 발급 이후 쿠키에 담는 과정 추가해야함
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.sendRedirect("http://localhost:3000");
    }

    public void setRefreshTokenToCookie(HttpServletRequest request ,OAuth2User oAuth2User) {
//        String refreshToken = tokenProvider.generateRefreshToken(oAuth2User.getAttribute("email"));
//        Cookie cookie = new Cookie("refresh_token", refreshToken);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(request.isSecure());
//        cookie.setPath("http://localhost:3000");
//
//        return cookie;
    }
}
