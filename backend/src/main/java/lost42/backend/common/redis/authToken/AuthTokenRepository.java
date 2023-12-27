package lost42.backend.common.redis.authToken;

import org.springframework.data.repository.CrudRepository;

public interface AuthTokenRepository extends CrudRepository<AuthToken, Long> {
}