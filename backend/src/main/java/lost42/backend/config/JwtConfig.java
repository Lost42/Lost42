package lost42.backend.config;

import lost42.backend.config.jwt.JwtProperties;
import lost42.backend.config.jwt.provider.TokenProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {

    @Bean(name = "tokenProvider")
    public TokenProvider tokenProvider(JwtProperties jwtProperties) {
        return new TokenProvider(
                jwtProperties.getSecret().getAccessSecretKey(),
                jwtProperties.getSecret().getRefreshSecretKey(),
                jwtProperties.getToken().getAccessExpiration(),
                jwtProperties.getToken().getRefreshExpiration()
        );
    }
}
