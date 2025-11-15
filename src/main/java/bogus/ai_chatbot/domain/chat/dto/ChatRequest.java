package bogus.ai_chatbot.domain.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ChatRequest {

    @Schema(description = "사용자가 보낼 메시지", example = "넌 이름이 뭐야?")
    private String prompt;
}
