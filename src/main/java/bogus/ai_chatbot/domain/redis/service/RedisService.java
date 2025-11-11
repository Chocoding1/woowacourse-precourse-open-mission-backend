package bogus.ai_chatbot.domain.redis.service;

import jakarta.transaction.Transactional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private static final Long EMAIL_EXPIRATION = 5L;
    private static final String AUTH_CODE_KEY_PREFIX = "auth:email:";

    @Value("${jwt.expiration-time.refresh}")
    private Long refreshTokenExpiration;
    private static final String REFRESH_TOKEN_KEY_PREFIX = "auth:refresh:";

    private final StringRedisTemplate stringRedisTemplate;

    public void saveEmailAuthCode(String email, String code) {
        stringRedisTemplate.opsForValue().set(AUTH_CODE_KEY_PREFIX + email, code, EMAIL_EXPIRATION, TimeUnit.MINUTES);
    }

    public String getEmailAuthCode(String email) {
        return stringRedisTemplate.opsForValue().get(AUTH_CODE_KEY_PREFIX + email);
    }

    public void saveRefreshToken(Long id, String refreshToken) {
        stringRedisTemplate.opsForValue().set(REFRESH_TOKEN_KEY_PREFIX + id, refreshToken, refreshTokenExpiration, TimeUnit.MILLISECONDS);
    }
}
