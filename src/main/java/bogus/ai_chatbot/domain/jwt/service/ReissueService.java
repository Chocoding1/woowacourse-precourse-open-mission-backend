package bogus.ai_chatbot.domain.jwt.service;

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
            throw new RuntimeException("토큰이 존재하지 않습니다.");
        }

        Long userId = jwtUtil.getId(refreshToken);

        validateToken(refreshToken, userId);

        return jwtUtil.createJwt(userId);
    }

    private void validateToken(String refreshToken, Long userId) {
        try {
            jwtUtil.validateToken(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refresh")) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        String savedRefreshToken = jwtUtil.getRefreshToken(userId);
        if (savedRefreshToken == null) {
            throw new RuntimeException("토큰이 존재하지 않습니다.");
        }

        if (!savedRefreshToken.equals(refreshToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
    }
}
