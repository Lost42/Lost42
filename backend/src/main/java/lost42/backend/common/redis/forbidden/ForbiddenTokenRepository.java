package lost42.backend.common.redis.forbidden;

import org.springframework.data.repository.CrudRepository;

public interface ForbiddenTokenRepository extends CrudRepository<ForbiddenToken, Long> {
    boolean existsByToken(String refreshToken);
}
