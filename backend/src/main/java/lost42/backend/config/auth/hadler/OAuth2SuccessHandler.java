package lost42.backend.config.auth.hadler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.config.auth.dto.CustomUserDetails;
import lost42.backend.config.jwt.JwtService;
import lost42.backend.domain.member.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("Oauth login success");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(oAuth2User.getAttribute("email"));
        String refreshToken = jwtService.generateRefreshToken(oAuth2User.getAttribute("email"));

        log.info("AccessToken: {}", accessToken);
        log.info("RefreshToken: {}", refreshToken);

        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setPath("/");

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(cookie);
        response.sendRedirect("/");
    }
}
