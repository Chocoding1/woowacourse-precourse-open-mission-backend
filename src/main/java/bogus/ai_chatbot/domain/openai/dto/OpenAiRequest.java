package bogus.ai_chatbot.domain.openai.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OpenAiRequest {

    private String model;
    private List<OpenAiMessage> messages;
}
