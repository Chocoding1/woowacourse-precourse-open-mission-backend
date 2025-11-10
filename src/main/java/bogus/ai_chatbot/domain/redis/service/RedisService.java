package bogus.ai_chatbot.domain.redis.service;

import jakarta.transaction.Transactional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RedisService {

    private static final Long EMAIL_EXPIRATION = 5L;
    private static final String AUTH_CODE_KEY_PREFIX = "auth:email:";

    private final StringRedisTemplate stringRedisTemplate;

    public void saveEmailAuthCode(String email, String code) {
        stringRedisTemplate.opsForValue().set(AUTH_CODE_KEY_PREFIX + email, code, EMAIL_EXPIRATION, TimeUnit.MINUTES);
    }

    public String getEmailAuthCode(String email) {
        return stringRedisTemplate.opsForValue().get(AUTH_CODE_KEY_PREFIX + email);
    }
}
