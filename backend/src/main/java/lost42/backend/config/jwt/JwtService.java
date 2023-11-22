package lost42.backend.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {
    @Value("${jwt.secretKey}")
    private String secret;

    @Value("${jwt.access.expiration}")
    private Long accessExpiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    public String generateAccessToken(String email) {
        Map<String, Object> headers = new HashMap<>();

        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Date expireDate = new Date();
        expireDate.setTime(expireDate.getTime() + accessExpiration);

        String jwt = Jwts.builder()
                .header().add(headers)
                .and()
                .issuer("lost42")
                .subject("access")
                .claim("email", email)
                .expiration(expireDate)
                .issuedAt(new Date())
                .signWith(key)
                .compact();

        return jwt;
    }

    public String generateRefreshToken(String email) {
        Date expireDate = new Date();
        expireDate.setTime(expireDate.getTime() + refreshExpiration);

        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        String jwt = Jwts.builder()
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(key)
                .compact();

        return jwt;
    }
}
