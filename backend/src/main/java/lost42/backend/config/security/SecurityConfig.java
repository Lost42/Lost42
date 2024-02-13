package lost42.backend.config.security;

import lombok.RequiredArgsConstructor;
import lost42.backend.common.auth.hadler.OAuth2SuccessHandler;
import lost42.backend.common.auth.service.CustomOauth2UserService;
import lost42.backend.common.jwt.handler.JwtAccessDeniedHandler;
import lost42.backend.common.jwt.handler.JwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomOauth2UserService customOauth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtSecurity jwtSecurity;
    private final String[] ignoreUrl = {
            "/", "/favicon.ico",
            "/oauth2/**"
//            "/swagger", "/swagger-ui/**", "/v3/api-docs/**",
//            "/api/v1/members/signup",
//            "/api/v1/members/reset-password", "/api/v1/members/signin",
//            "/api/v1/jwt/test",
//            "/api/v1/members/find-email", "/api/v1/members/find-password", "/api/v1/members/auth",
//            "/api/v1/boards/get"
    };
    private List<String> corsOrigins = List.of("http://localhost:3000");


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .cors()
                    .configurationSource(corsConfigurationSource())
                .and()
                .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .logout()
                    .logoutUrl("/api/v1/member/logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                .and()
                .authorizeRequests(authorize ->
                        authorize.antMatchers(ignoreUrl).permitAll()
                )
                .apply(jwtSecurity)
                .and()
                .oauth2Login()
                    .successHandler(oAuth2SuccessHandler)
                    .userInfoEndpoint().userService(customOauth2UserService);

        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsOrigins);
        configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setMaxAge(3600L);
//        configuration.setExposedHeaders(List.of(SET_COOKIE, "accessToken", AuthConstants.REFRESH_TOKEN.getValue()));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}

