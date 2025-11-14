package bogus.ai_chatbot.domain.chat.entity.field;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {

    USER("user"),
    ASSISTANT("assistant");

    private final String role;
}
