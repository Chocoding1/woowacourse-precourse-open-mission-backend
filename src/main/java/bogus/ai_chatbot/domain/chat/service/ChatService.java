package bogus.ai_chatbot.domain.chat.service;

import static bogus.ai_chatbot.domain.chat.entity.field.Role.*;
import static bogus.ai_chatbot.domain.common.exception.error.ErrorCode.CONVERSATION_NOT_FOUND;

import bogus.ai_chatbot.domain.chat.entity.Conversation;
import bogus.ai_chatbot.domain.chat.entity.Message;
import bogus.ai_chatbot.domain.chat.entity.field.Role;
import bogus.ai_chatbot.domain.chat.repository.ConversationRepository;
import bogus.ai_chatbot.domain.chat.repository.MessageRepository;
import bogus.ai_chatbot.domain.common.exception.exception.ChatException;
import bogus.ai_chatbot.domain.openai.dto.OpenAiResponse;
import bogus.ai_chatbot.domain.openai.service.OpenAiClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final OpenAiClient openAiClient;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    // 새로운 Conversation으로 요청 + 로그인 X
    public String getResponseMessageWhenGuest(String prompt) {
        return getResponseMessage(prompt);
    }

    // 기존 Conversation으로 요청(conversationId 필수)
    @Transactional
    public String getResponseMessageWhenMember(Long conversationId, String prompt) {
        Conversation conversation = getConversation(conversationId);

        Message requestMessage = createMessage(prompt, conversation, USER);

        String initialResponseMessage = getResponseMessage(prompt);

        Message responseMessage = createMessage(initialResponseMessage, conversation, ASSISTANT);

        messageRepository.saveAll(List.of(requestMessage, responseMessage));

        conversation.updateModifiedAt(responseMessage.getCreatedAt());

        return initialResponseMessage;
    }

    private Conversation getConversation(Long conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ChatException(CONVERSATION_NOT_FOUND));
    }

    private String getResponseMessage(String prompt) {
        OpenAiResponse openAiResponse = openAiClient.getAiResponse(prompt);
        return openAiResponse.getChoices().getFirst().getMessage().getContent();
    }

    private static Message createMessage(String prompt, Conversation conversation, Role role) {
        return Message.builder()
                .content(prompt)
                .conversation(conversation)
                .role(role)
                .build();
    }
}
