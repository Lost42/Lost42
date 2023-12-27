package lost42.backend.common.jwt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.jwt.provider.TokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/jwt")
public class JwtController {
    private final TokenProvider tokenProvider;

    @GetMapping("/token")
    public ResponseEntity<?> getToken() {

        return ResponseEntity.ok().body("");
    }
}
