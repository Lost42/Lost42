package lost42.backend.common.jwt.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.jwt.dto.JwtTokenInfo;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
     *  accessToken 생성, 안쪽 정보 (id, role, oauthId)
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
     * refreshToken 생성, 안쪽 정보 (id, role, oauthId)
     */
    public String generateRefreshToken(JwtTokenInfo tokenInfo) {
        return Jwts.builder()
                .header()
                .add(createHeaders())
                .and()
                .claims(createClaims("lost42-refresh", tokenInfo))
                .expiration(createExpiration(refreshExpiration))
                .issuedAt(new Date())
                .signWith(refreshSecretKey)
                .compact();
    }

    /**
     * token을 Authentication 객체로 변환
     */
    public Long getIdWithAccessToken(String token) {
        Claims claims = Jwts.parser().verifyWith(accessSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Long memberId = claims.get("id", Long.class);
        return memberId;
    }

    public Long getIdWithRefreshToken(String token) {
        Claims claims = Jwts.parser().verifyWith(refreshSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Long memberId = claims.get("id", Long.class);
        return memberId;
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
