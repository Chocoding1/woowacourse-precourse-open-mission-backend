package bogus.ai_chatbot.domain.email.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
public class EmailDto {

    @Schema(description = "이메일", example = "email@naver.com")
    private String email;
    @Schema(description = "이메일 인증코드", example = "123456")
    private String authCode;
}
