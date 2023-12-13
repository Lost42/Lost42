package lost42.backend.common.redis.refreshtoken;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.jwt.provider.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
@Slf4j
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final Duration refreshTokenExpiration;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               TokenProvider tokenProvider,
                               @Value("${jwt.token.refresh-expiration}") Duration refreshTokenExpiration)
    {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public void saveRefreshToken(String refreshToken) {
        RefreshToken save = RefreshToken.builder()
                .memberId(tokenProvider.getIdWithRefreshToken(refreshToken))
                .token(refreshToken)
                .build();

        refreshTokenRepository.save(save);
    }

//    public String reissueRefreshToken(String accessToken) {
//        RefreshToken refreshToken = RefreshToken.builder()
//                .memberId(tokenProvider.getId(accessToken))
//                .token(accessToken)
//                .build();
//
//        return "";
//    }

    public boolean isValidRefreshToken(String refreshToken) {
        RefreshToken saved = refreshTokenRepository.findById(tokenProvider.getIdWithRefreshToken(refreshToken))
                .orElse(null);

        if (saved == null) {
            log.warn("RefreshToken is not found");
            return false;
        }

        if (isRefreshTokenExpired(saved)) {
            log.warn("Refresh token has expired");
            return false;
        }

        return true;
    }

    private boolean isRefreshTokenExpired(RefreshToken refreshToken) {
        return refreshToken.getTtl() <= System.currentTimeMillis();
    }
}
