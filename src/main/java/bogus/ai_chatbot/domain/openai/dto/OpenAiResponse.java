package bogus.ai_chatbot.domain.openai.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OpenAiResponse {

    private List<Choice> choices;

    @Getter
    @Builder
    public static class Choice {
        private OpenAiMessage message;
    }
}
