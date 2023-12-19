package lost42.backend.common.auth.hadler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.jwt.dto.JwtTokenInfo;
import lost42.backend.common.jwt.provider.TokenProvider;
import lost42.backend.common.auth.dto.CustomOAuth2User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
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
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        JwtTokenInfo tokenInfo = JwtTokenInfo.fromOAuth2User(oAuth2User);
        String accessToken = tokenProvider.generateAccessToken(tokenInfo);
        String refreshToken = tokenProvider.generateRefreshToken(tokenInfo);

        response.addHeader("Authorization", "Bearer " + accessToken);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("http://localhost:3000")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.sendRedirect("http://localhost:3000");
    }
}
