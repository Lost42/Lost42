package lost42.backend.common.jwt.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.jwt.dto.JwtTokenInfo;
import lost42.backend.config.auth.MemberRole;
import lost42.backend.config.auth.dto.CustomUserDetails;
import lost42.backend.domain.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    public String generateAccessToken(JwtTokenInfo tokenInfo) {
        return Jwts.builder()
                .header()
                .add(createHeaders())
                .and()
                .claims(createClaims("lost42-access", tokenInfo))
                .expiration(createExpiration(accessExpiration))
                .issuedAt(new Date())
                .signWith(accessSecretKey)
                .compact();
    }

    /**
     * refreshToken 생성, 안쪽 정보(email, role)
     */
    public String generateRefreshToken(JwtTokenInfo tokenInfo) {
        return Jwts.builder()
                .header()
                .add(createHeaders())
                .and()
                .claims(createClaims("lost42-refresh", tokenInfo))
                .expiration(createExpiration(refreshExpiration))
                .issuedAt(new Date())
                .signWith(accessSecretKey)
                .compact();
    }

    /**
     * 액세스 토큰 유효성 검증
     */
    public boolean validateAccessToken(String token) {
        Claims claims = Jwts.parser().verifyWith(accessSecretKey).build()
                .parseSignedClaims(token).getPayload();

        return claims.getSubject().equals("lost42-access");
    }

    public boolean validateRefreshToken(String token) {
        Claims claims = Jwts.parser().verifyWith(refreshSecretKey).build()
                .parseSignedClaims(token).getPayload();

        return claims.getSubject().equals("lost42-refresh");
    }

    /**
     * token을 Authentication 객체로 변환
     */
    public String getEmail(String token) {
        Claims claims = Jwts.parser().verifyWith(accessSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String email = claims.get("email", String.class);
        return email;
    }

    public Map<String, Object> createHeaders() {
        Map<String, Object> headers = new HashMap<>();

        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        return headers;
    }

    public Date createExpiration(Long duration) {
        Date expireDate = new Date();

        expireDate.setTime(expireDate.getTime() + duration);
        return expireDate;
    }

    public Map<String, Object> createClaims(String subject, JwtTokenInfo tokenInfo) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("iss", "lost42");
        claims.put("sub", subject);
        claims.put("id", tokenInfo.getMemberId());
        claims.put("role", tokenInfo.getRole());
        claims.put(tokenInfo.getOauthProvider() + "Id", tokenInfo.getOauthId());

        return claims;
    }
}
