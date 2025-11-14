package bogus.ai_chatbot.domain.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class OpenAiMessage {

    private String role;
    private String content;
}
