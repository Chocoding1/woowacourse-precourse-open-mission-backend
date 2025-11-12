package bogus.ai_chatbot.domain.jwt.util;

import static bogus.ai_chatbot.domain.exception.error.ErrorCode.INVALID_TOKEN;
import static bogus.ai_chatbot.domain.exception.error.ErrorCode.TOKEN_EXPIRED;

import bogus.ai_chatbot.domain.exception.CustomException;
import bogus.ai_chatbot.domain.jwt.dto.JwtInfoDto;
import bogus.ai_chatbot.domain.redis.service.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessTokenExpireMs;
    private final Long refreshTokenExpireMs;
    private final RedisService redisService;

    public JwtUtil(@Value("${jwt.secret-key}") String secretKey,
                   @Value("${jwt.expiration-time.access}") Long accessTokenExpireMs,
                   @Value("${jwt.expiration-time.refresh}") Long refreshTokenExpireMs,
                   RedisService redisService) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpireMs = accessTokenExpireMs;
        this.refreshTokenExpireMs = refreshTokenExpireMs;
        this.redisService = redisService;
    }

    public Long getId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("id", Long.class);
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get("category", String.class);
    }

    public String getRefreshToken(Long id) {
        return redisService.getRefreshToken(id);
    }

    public void validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            throw new CustomException(TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new CustomException(INVALID_TOKEN);
        }
    }

    public void validateAccessCategory(String accessToken) {
        String category = getCategory(accessToken);
        if (!category.equals("access")) {
            throw new CustomException(INVALID_TOKEN);
        }
    }

    public void validateRefreshCategory(String refreshToken) {
        String category = getCategory(refreshToken);
        if (!category.equals("refresh")) {
            throw new CustomException(INVALID_TOKEN);
        }
    }

    public void deleteRefreshToken(Long userId) {
        redisService.deleteRefreshToken(userId);
    }

    public JwtInfoDto createJwt(Long id) {
        String accessToken = createAccessToken(id);
        String refreshToken = createRefreshToken(id);

        return JwtInfoDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String createAccessToken(Long id) {
        return Jwts.builder()
                .claim("category", "access")
                .claim("id", id)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpireMs))
                .signWith(secretKey)
                .compact();
    }

    private String createRefreshToken(Long id) {
        String refreshToken = Jwts.builder()
                .claim("category", "refresh")
                .claim("id", id)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpireMs))
                .signWith(secretKey)
                .compact();

        redisService.saveRefreshToken(id, refreshToken);

        return refreshToken;
    }
}
