package lost42.backend.config.jwt.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.config.auth.MemberRole;
import lost42.backend.config.auth.dto.CustomUserDetails;
import lost42.backend.domain.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Token 검증 및 생성, 토큰에서 정보를 꺼내 Spring Security Authentication 객체 생성하는 역할
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {
    private final String accessSecret;
    private final String refreshSecret;
    private final Long accessExpiration;
    private final Long refreshExpiration;
    private SecretKey accessSecretKey;
    private SecretKey refreshSecretKey;


    @PostConstruct
    private void init() {
        this.accessSecretKey = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        this.refreshSecretKey = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     *  accessToken 생성, 안쪽 정보 (email, role)
     */
    public String generateAccessToken(Authentication authentication) {
        CustomUserDetails securityUser = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> headers = new HashMap<>();

        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Date expireDate = new Date();
        expireDate.setTime(expireDate.getTime() + accessExpiration);

        String accessToken = Jwts.builder()
                .header()
                    .add(headers)
                .and()
                .issuer("lost42")
                .subject("lost42-access")
                .claim("email", securityUser.getUserEmail())
                .claim("role", securityUser.getAuthorities().iterator().next().getAuthority().substring("ROLE_".length()))
                .expiration(expireDate)
                .issuedAt(new Date())
                .signWith(accessSecretKey)
                .compact();

        return accessToken;
    }

    /**
     * refreshToken 생성, 안쪽 정보(email, role)
     */
    public String generateRefreshToken(Authentication authentication) {
        CustomUserDetails securityUser = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> headers = new HashMap<>();

        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Date expireDate = new Date();
        expireDate.setTime(expireDate.getTime() + refreshExpiration);

        String refreshToken = Jwts.builder()
                .header()
                    .add(headers)
                .and()
                .issuer("lost42")
                .subject("lost42-refresh")
                .claim("email", securityUser.getUserEmail())
                .claim("role", securityUser.getAuthorities().iterator().next().getAuthority().substring("ROLE_".length()))
                .expiration(expireDate)
                .issuedAt(new Date())
                .signWith(refreshSecretKey)
                .compact();

        return refreshToken;
    }

    /**
     * 액세스 토큰 유효성 검증
     */
    public boolean validateAccessToken(String token) {
        Jwts.parser().decryptWith(accessSecretKey)
                .build()
                .parseSignedClaims("email")
                .getPayload()
                .getSubject()
                .equals("lost42-access");

        return true;
    }

    public boolean validateRefreshToken(String token) {
        Jwts.parser().decryptWith(refreshSecretKey)
                .build()
                .parseSignedClaims("email")
                .getPayload()
                .getSubject()
                .equals("lost42-refresh");

        return true;
    }

    /**
     * token을 Authentication 객체로 변환
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().verifyWith(accessSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(role.toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        Member principal = Member.builder()
                .email(email)
                .role(MemberRole.fromString(role))
                .build();

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
