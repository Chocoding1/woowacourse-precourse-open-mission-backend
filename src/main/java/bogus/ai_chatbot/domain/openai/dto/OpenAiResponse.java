package bogus.ai_chatbot.domain.openai.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class OpenAiResponse {

    private List<Choice> choices;

    @Override
    public String toString() {
        return "OpenAiResponse{" +
                "choices=" + choices +
                '}';
    }

    @Getter
    public static class Choice {
        private OpenAiMessage message;
    }
}
