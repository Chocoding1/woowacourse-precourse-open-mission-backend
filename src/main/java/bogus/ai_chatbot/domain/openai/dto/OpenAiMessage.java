package bogus.ai_chatbot.domain.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OpenAiMessage {

    private String role;
    private String content;

    @Override
    public String toString() {
        return "OpenAiMessage{" +
                "role='" + role + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
