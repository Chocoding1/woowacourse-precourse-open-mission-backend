package bogus.ai_chatbot.domain.chat.dto;

import lombok.Getter;

@Getter
public class ChatResponse {

    private String responseMessage;

    private ChatResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public static ChatResponse of(String responseMessage) {
        return new ChatResponse(responseMessage);
    }
}
