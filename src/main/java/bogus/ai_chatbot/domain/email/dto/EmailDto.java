package bogus.ai_chatbot.domain.email.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailDto {

    private String email;
    private String verificationCode;
}
