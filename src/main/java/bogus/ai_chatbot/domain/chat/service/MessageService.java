package bogus.ai_chatbot.domain.chat.service;

import bogus.ai_chatbot.domain.chat.dto.MessageDto;
import bogus.ai_chatbot.domain.chat.dto.MessagesDto;
import bogus.ai_chatbot.domain.chat.entity.Conversation;
import bogus.ai_chatbot.domain.chat.entity.Message;
import bogus.ai_chatbot.domain.chat.repository.ConversationRepository;
import bogus.ai_chatbot.domain.chat.repository.MessageRepository;
import bogus.ai_chatbot.domain.common.exception.error.ErrorCode;
import bogus.ai_chatbot.domain.common.exception.exception.ChatException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    public MessagesDto getMessages(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ChatException(ErrorCode.CONVERSATION_NOT_FOUND));

        List<Message> messages = messageRepository.findByConversationOrderByCreatedAtAsc(
                conversation);

        List<MessageDto> messageDtos = messages.stream()
                .map(message -> new MessageDto(message.getRole(), message.getContent()))
                .toList();

        return new MessagesDto(messageDtos);
    }
}
