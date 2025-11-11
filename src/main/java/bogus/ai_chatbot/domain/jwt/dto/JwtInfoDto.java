package bogus.ai_chatbot.domain.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtInfoDto {

    private String accessToken;
    private String refreshToken;
}
