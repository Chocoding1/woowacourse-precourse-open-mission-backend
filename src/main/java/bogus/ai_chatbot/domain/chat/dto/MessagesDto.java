package bogus.ai_chatbot.domain.chat.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessagesDto {

    private List<MessageDto> messageDtos;
}
