package lost42.backend.domain.member.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.jwt.dto.JwtTokenInfo;
import lost42.backend.common.jwt.provider.TokenProvider;
import lost42.backend.common.redis.refreshtoken.RefreshTokenService;
import lost42.backend.common.auth.dto.CustomUserDetails;
import lost42.backend.domain.member.dto.LoginReq;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class LogInService {
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public String[] login(LoginReq req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUserEmail(), req.getUserPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtTokenInfo tokenInfo = JwtTokenInfo.fromCustomUserDetails((CustomUserDetails) authentication.getPrincipal());

        String accessToken = tokenProvider.generateAccessToken(tokenInfo);
        String refreshToken = tokenProvider.generateRefreshToken(tokenInfo);

        refreshTokenService.saveRefreshToken(refreshToken);

        return new String[]{accessToken, refreshToken};
    }

}
