package bogus.ai_chatbot.domain.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ChatResponse {

    @Schema(description = "AI 응답 메시지", example = "제 이름은.. 맞춰보세요ㅎ")
    private final String message;
    @Schema(description = "새로운 채팅방인 경우 redirect할 URI", example = "/chats/123")
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
