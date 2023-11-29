package lost42.backend.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lost42.backend.config.JwtConfig;
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
