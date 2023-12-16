package lost42.backend.common.redis.forbidden;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("forbiddenToken")
@Builder
@Getter
@ToString(of = {"id", "token"})
public class ForbiddenToken {
    @Id
    private final Long id;
    private final String token;

    @Builder
    public ForbiddenToken(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    public static ForbiddenToken fromToken(String token) {
        return ForbiddenToken.builder()
                .token(token)
                .build();
    }
}
