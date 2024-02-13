package lost42.backend.common.redis.forbidden;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForbiddenTokenService {
    private final ForbiddenTokenRepository forbiddenTokenRepository;

    public void expireToken(String token) {
        ForbiddenToken forbiddenToken = ForbiddenToken.fromToken(token);
        forbiddenTokenRepository.save(forbiddenToken);
    }

    public boolean isExist(String refreshToken) {
        log.warn("forbiddenTokenService: {}", refreshToken);
        return forbiddenTokenRepository.existsByToken(refreshToken);
    }
}
