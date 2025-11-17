package bogus.ai_chatbot.domain.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ChatResponse {

    @Schema(description = "AI 응답 메시지", example = "제 이름은.. 맞춰보세요ㅎ")
    private final String message;
    @Schema(description = "새로운 채팅방 ID", example = "123")
    private final Long chatId;

    private ChatResponse(String message, Long chatId) {
        this.message = message;
        this.chatId = chatId;
    }

    public static ChatResponse from(String message) {
        return new ChatResponse(message, null);
    }

    public static ChatResponse of(String message, Long chatId) {
        return new ChatResponse(message, chatId);
    }
}
