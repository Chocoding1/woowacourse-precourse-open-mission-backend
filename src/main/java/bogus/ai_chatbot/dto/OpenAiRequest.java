package bogus.ai_chatbot.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OpenAiRequest {

    private String model;
    private List<OpenAiMessage> messages;

    @Override
    public String toString() {
        return "OpenAiRequest{" +
                "model='" + model + '\'' +
                ", messages=" + messages +
                '}';
    }
}
