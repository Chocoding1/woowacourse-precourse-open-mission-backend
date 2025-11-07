package bogus.ai_chatbot.dto;

import lombok.Getter;

@Getter
public class ChatRequest {

    private String message;

    @Override
    public String toString() {
        return "ChatRequest{" +
                "message='" + message + '\'' +
                '}';
    }
}
