package lost42.backend.common.redis.authToken;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("authToken")
@Getter
@ToString(of = {"memberId", "token"})
public class AuthToken {
    @Id
    private final Long memberId;
    private final String token;
    @TimeToLive
    private long ttl;

    @Builder
    public AuthToken(Long memberId, String token) {
        this.memberId = memberId;
        this.token = token;
        this.ttl = 300;
    }

    public static AuthToken fromToken(Long memberId, String token) {
        return AuthToken.builder()
                .memberId(memberId)
                .token(token)
                .build();
    }
}
