package lost42.backend.common.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private Secret secret;
    private Token token;

    @Data
    public static class Secret {
        private String accessSecretKey;
        private String refreshSecretKey;
    }

    @Data
    public static class Token {
        private Long accessExpiration;
        private String accessHeader;
        private Long refreshExpiration;
    }
}
