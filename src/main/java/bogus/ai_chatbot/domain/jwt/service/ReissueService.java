package bogus.ai_chatbot.domain.jwt.service;

import static bogus.ai_chatbot.domain.exception.error.ErrorCode.INVALID_TOKEN;
import static bogus.ai_chatbot.domain.exception.error.ErrorCode.TOKEN_EXPIRED;
import static bogus.ai_chatbot.domain.exception.error.ErrorCode.TOKEN_NULL;

import bogus.ai_chatbot.domain.exception.CustomException;
import bogus.ai_chatbot.domain.jwt.dto.JwtInfoDto;
import bogus.ai_chatbot.domain.jwt.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtUtil jwtUtil;

    public JwtInfoDto reissueToken(String refreshToken) {
        if (refreshToken == null) {
            throw new CustomException(TOKEN_NULL);
        }

        Long userId = jwtUtil.getId(refreshToken);

        validateToken(refreshToken, userId);

        return jwtUtil.createJwt(userId);
    }

    private void validateToken(String refreshToken, Long userId) {
        try {
            jwtUtil.validateToken(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new CustomException(TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new CustomException(INVALID_TOKEN);
        }

        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refresh")) {
            throw new CustomException(INVALID_TOKEN);
        }

        String savedRefreshToken = jwtUtil.getRefreshToken(userId);
        if (savedRefreshToken == null) {
            throw new CustomException(TOKEN_NULL);
        }

        if (!savedRefreshToken.equals(refreshToken)) {
            throw new CustomException(INVALID_TOKEN);
        }
    }
}
