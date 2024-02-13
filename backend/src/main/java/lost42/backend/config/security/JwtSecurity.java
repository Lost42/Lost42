package lost42.backend.config.security;

import lombok.RequiredArgsConstructor;
import lost42.backend.common.jwt.filter.CustomJwtFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class JwtSecurity extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final CustomJwtFilter customJwtFilter;
    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(customJwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
