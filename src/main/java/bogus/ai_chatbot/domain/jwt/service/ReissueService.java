package bogus.ai_chatbot.domain.jwt.service;

import static bogus.ai_chatbot.domain.exception.error.ErrorCode.TOKEN_NULL;

import bogus.ai_chatbot.domain.exception.exception.CustomAuthException;
import bogus.ai_chatbot.domain.jwt.dto.JwtInfoDto;
import bogus.ai_chatbot.domain.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtUtil jwtUtil;

    public JwtInfoDto reissueToken(String refreshToken) {
        if (refreshToken == null) {
            throw new CustomAuthException(TOKEN_NULL);
        }

        validateToken(refreshToken);

        Long userId = jwtUtil.getId(refreshToken);
        return jwtUtil.createJwt(userId);
    }

    private void validateToken(String refreshToken) {
        jwtUtil.validateToken(refreshToken);
        jwtUtil.validateRefreshCategory(refreshToken);
        jwtUtil.validateTokenSame(refreshToken);
    }
}
