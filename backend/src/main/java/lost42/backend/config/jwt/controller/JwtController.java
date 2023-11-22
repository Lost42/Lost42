package lost42.backend.config.jwt.controller;

import lombok.RequiredArgsConstructor;
import lost42.backend.config.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jwt")
public class JwtController {
    private final JwtService jwtService;

    @GetMapping("/test")
    public String tokenTest(HttpServletResponse response) {
//        String token = jwtService.createAccessToken();
//        response.setHeader("Authorization", token);

        return "Success";
    }
}
