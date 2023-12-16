package lost42.backend.domain.member.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.auth.dto.CustomUserDetails;
import lost42.backend.common.auth.exception.AuthErrorCode;
import lost42.backend.common.auth.exception.AuthErrorException;
import lost42.backend.common.redis.forbidden.ForbiddenToken;
import lost42.backend.common.redis.forbidden.ForbiddenTokenRepository;
import lost42.backend.common.redis.refreshtoken.RefreshToken;
import lost42.backend.common.redis.refreshtoken.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class LogOutService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final ForbiddenTokenRepository forbiddenTokenRepository;

    public void logOut(CustomUserDetails securityUser) {
        Long memberId = securityUser.getMemberId();

        RefreshToken refreshToken = refreshTokenRepository.findById(memberId)
                .orElseThrow(() -> new AuthErrorException(AuthErrorCode.EMPTY_REFRESH_TOKEN, "리프레시 토큰이 비어있습니다"));

        ForbiddenToken forbiddenRefreshToken = ForbiddenToken.fromToken(refreshToken.getToken());

        forbiddenTokenRepository.save(forbiddenRefreshToken);
        refreshTokenRepository.delete(refreshToken);
    }
}
