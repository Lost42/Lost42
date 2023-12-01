package lost42.backend.config.auth.controller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.jwt.dto.JwtTokenInfo;
import lost42.backend.config.auth.dto.CustomUserDetails;
import lost42.backend.config.auth.service.AuthService;
import lost42.backend.common.jwt.provider.TokenProvider;
import lost42.backend.domain.member.dto.LoginReq;
import lost42.backend.domain.member.entity.Member;
import lost42.backend.domain.member.repository.MemberRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Getter
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUserEmail(), req.getUserPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtTokenInfo tokenInfo = JwtTokenInfo.fromCustomUserDetails((CustomUserDetails) authentication.getPrincipal());

        String accessToken = tokenProvider.generateAccessToken(tokenInfo);
        String refreshToken = tokenProvider.generateRefreshToken(tokenInfo);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .build();

        return ResponseEntity.ok().headers(headers).header("Set-Cookie", cookie.toString()).body("");
    }

}
