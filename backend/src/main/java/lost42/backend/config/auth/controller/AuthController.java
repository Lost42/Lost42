package lost42.backend.config.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.config.auth.service.AuthService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Getter
@Slf4j
public class AuthController {
    private final AuthService authService;

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

    // http://localhost:8080/login/oauth2/code/42Seoul
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
