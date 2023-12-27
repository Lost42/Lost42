package lost42.backend.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lost42.backend.common.auth.dto.CustomUserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenUtil {
    private final String accessSecret;
    private final String refreshSecret;
    private SecretKey accessSecretKey;
    private SecretKey refreshSecretKey;

    @PostConstruct
    private void init() {
        this.accessSecretKey = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        this.refreshSecretKey = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
    }

    // 헤더에서 accessToken 추출
    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 쿠키에서 refreshToken 추출
    public String resolveRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName()))
                    return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 액세스 토큰을 이용
     */
    // 토큰의 유효성 검사
    public boolean validateAccessToken(String accessToken, CustomUserDetails userDetails) {
        final Long memberId = extractMemberIdWithAccessToken(accessToken);
        return (memberId == userDetails.getMemberId());
    }

    public Date extractExpirationWithAccessToken(String accessToken) {
        return extractClaimsWithAccessToken(accessToken).getExpiration();
    }

    // 토큰이 만료되었는지 확인
    public Boolean isTokenExpiredWithAccessToken(String accessToken) {
        final Date expiration = extractExpirationWithAccessToken(accessToken);
        return expiration.before(new Date());
    }

    private Claims extractClaimsWithAccessToken(String accessToken) {
        return Jwts.parser().verifyWith(accessSecretKey).build()
                .parseSignedClaims(accessToken).getPayload();
    }

    private Long extractMemberIdWithAccessToken(String accessToken) {
        return extractClaimsWithAccessToken(accessToken).get("id", Long.class);
    }

    /**
     * 리프레시 토큰을 이용
     */
    // 토큰의 유효성 검사
    public Boolean validateRefreshToken(String refreshToken, CustomUserDetails userDetails) {
        final Long memberId = extractMemberIdWithRefreshToken(refreshToken);
        return (memberId == userDetails.getMemberId());
    }

    public Date extractExpirationWithRefreshToken(String refreshToken) {
        return extractClaimsWithRefreshToken(refreshToken).getExpiration();
    }

    // 토큰이 만료되었는지 확인
    public Boolean isTokenExpiredWithRefreshToken(String refreshToken) {
        final Date expiration = extractExpirationWithRefreshToken(refreshToken);
        return expiration.before(new Date());
    }

    private Claims extractClaimsWithRefreshToken(String refreshToken) {
        return Jwts.parser().verifyWith(refreshSecretKey).build()
                .parseSignedClaims(refreshToken).getPayload();
    }

    private Long extractMemberIdWithRefreshToken(String refreshToken) {
        return extractClaimsWithRefreshToken(refreshToken).get("id", Long.class);
    }
}
