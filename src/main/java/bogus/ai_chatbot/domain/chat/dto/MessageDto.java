package bogus.ai_chatbot.domain.chat.dto;

import bogus.ai_chatbot.domain.chat.entity.field.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageDto {

    private Role role;
    private String content;
}
