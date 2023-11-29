package lost42.backend.config.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.config.auth.dto.CustomUserDetails;
import lost42.backend.config.auth.service.AuthService;
import lost42.backend.config.jwt.provider.TokenProvider;
import lost42.backend.domain.member.dto.LoginReq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Getter
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;


    @Value("${spring.security.oauth2.client.registration.42Seoul.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.42Seoul.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.42Seoul.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.42Seoul.authorization-uri}")
    private String authorizationUri;

    @Value("${spring.security.oauth2.client.provider.42Seoul.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.42Seoul.user-info-uri}")
    private String userInfoUri;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUserEmail(), req.getUserPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails securityUser = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .build();

        return ResponseEntity.ok().headers(headers).header("Set-Cookie", cookie.toString()).body("");
    }

//     http://localhost:8080/login/oauth2/code/42Seoul
    @GetMapping("/login/42") // 임시로, 나중에 프론트에서 httPs://localhost:8080/oauth2/authorization/42Seoul 링크로 접속하게 설정
    public ResponseEntity<?> FortyTwoOauthLogin() {
        final String authorizeUrl = String.format("%s?client_id=%s&redirect_uri=%s&response_type=code&scope=public&state=veryverylonglongnumber", authorizationUri, clientId, redirectUri);

        log.info("url: {}", URI.create(authorizeUrl));
        return ResponseEntity.status(302).location(URI.create(authorizeUrl)).build();
    }

    @GetMapping("/42/callback")
    public ResponseEntity<?> FortyTwoCallBack(@RequestParam String code) throws JsonProcessingException {
        final String getTokenUrl = String.format("%s?grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&redirect_uri=%s",
                tokenUri, clientId, clientSecret, code, redirectUri);

        String accessToken = authService.getFortyTwoAccessToken(getTokenUrl);
        ResponseEntity<String> userInfo = authService.getFortyTwoUserInfo(userInfoUri, accessToken);


        return ResponseEntity.ok().body("");
    }

}
