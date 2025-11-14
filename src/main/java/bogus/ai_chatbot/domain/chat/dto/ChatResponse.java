package bogus.ai_chatbot.domain.chat.dto;

import lombok.Getter;

@Getter
public class ChatResponse {

    private final String message;
    private final String redirectUri;

    private ChatResponse(String message, String redirectUri) {
        this.message = message;
        this.redirectUri = redirectUri;
    }

    public static ChatResponse from(String message) {
        return new ChatResponse(message, null);
    }

    public static ChatResponse of(String message, String redirectUri) {
        return new ChatResponse(message, redirectUri);
    }
}
