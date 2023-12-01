package lost42.backend.common.jwt.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.jwt.provider.TokenProvider;
import lost42.backend.config.auth.service.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomJwtFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final List<String> jwtIgnoreUrl = List.of(
            "/swagger", "/swagger-ui/**", "/v3/api-docs/**",
            "/api/v1/auth/login", "/oauth2/**"
    );

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (ignoreTokenRequest(request)) {
            log.info("JWT Filer: Ignoring request: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        log.warn("Request: {}", request);
        String accessToken = resolveToken(request);

        UserDetails userDetails = getUserDetails(accessToken);
        authenticationUser(userDetails, request);
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean ignoreTokenRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        Optional<String> judge = jwtIgnoreUrl.stream()
                .filter(v ->
                        Pattern.matches(v.replace("**", ".*"), uri) ||
                                Pattern.matches(v.replace("/**", ""), uri)

                )
                .findFirst();
        return !judge.isEmpty() || "OPTIONS".equals(method);
    }

    private UserDetails getUserDetails(String accessToken) {
        String email = tokenProvider.getEmail(accessToken);
        return customUserDetailsService.loadUserByUsername(email);
    }

    private void authenticationUser(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Authenticated user: {}", userDetails.getUsername());
    }
}
