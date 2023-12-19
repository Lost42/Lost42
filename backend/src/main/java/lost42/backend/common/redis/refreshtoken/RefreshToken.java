package lost42.backend.common.redis.refreshtoken;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.Duration;

@RedisHash("refreshToken")
@Getter
@ToString(of = {"memberId", "token"})
public class RefreshToken {
    @Id
    private final Long memberId;
    private final String token;
    @TimeToLive
    private final long ttl;

    @Builder
    private RefreshToken(String token, Long memberId, long ttl) {
        this.token = token;
        this.memberId = memberId;
        this.ttl = ttl;
    }
}
