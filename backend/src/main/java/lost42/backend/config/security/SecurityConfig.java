package lost42.backend.config.security;

import lombok.RequiredArgsConstructor;
//import lost42.backend.config.auth.hadler.OAuth2SuccessHandler;
import lost42.backend.config.auth.hadler.OAuth2SuccessHandler;
import lost42.backend.config.auth.service.CustomOauth2UserService;
import lost42.backend.config.auth.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOauth2UserService customOauth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests(authorize ->
                        authorize.antMatchers("/", "/**").permitAll()
                                .antMatchers("/api/v1/**").permitAll()
                                .antMatchers("/favicon.ico").permitAll()
                                .antMatchers("/oauth2/**").permitAll()
                                .antMatchers("/login/oauth2/**").permitAll()
                                .anyRequest().authenticated())
                .oauth2Login()
                .successHandler(oAuth2SuccessHandler)
                .userInfoEndpoint().userService(customOauth2UserService);

        return httpSecurity.build();
    }
}
