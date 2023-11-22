package lost42.backend.config.auth.controller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Getter
@Slf4j
public class AuthController {

    private final HttpSession httpSession;

    @Value("${spring.security.oauth2.client.registration.42Seoul.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.42Seoul.redirect-uri}")
    private String redirectUri;

    @GetMapping("/login/42") // 임시로, 나중에 프론트에서 httPs://localhost:8080/oauth2/authorization/42Seoul 링크로 접속하게 설정
    public ResponseEntity<?> FortyTwoOauthLogin() {
        final String basicAuthorizeUrl = "https://api.intra.42.fr/oauth/authorize";
        final String authorizeUrl = String.format("%s?client_id=%s&redirect_uri=%s&response_type=code", basicAuthorizeUrl, clientId, redirectUri);
        log.info("Redirect to {}", authorizeUrl);

        return ResponseEntity.status(302).location(URI.create(authorizeUrl)).build();
    }
}
