package bogus.ai_chatbot.domain.chat.service;

import static bogus.ai_chatbot.domain.common.exception.error.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import bogus.ai_chatbot.domain.chat.dto.MessagesDto;
import bogus.ai_chatbot.domain.chat.entity.Conversation;
import bogus.ai_chatbot.domain.chat.entity.Message;
import bogus.ai_chatbot.domain.chat.repository.ConversationRepository;
import bogus.ai_chatbot.domain.chat.repository.MessageRepository;
import bogus.ai_chatbot.domain.common.exception.error.ErrorCode;
import bogus.ai_chatbot.domain.common.exception.exception.ChatException;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ErrorCollector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @InjectMocks
    MessageService messageService;

    @Mock
    MessageRepository messageRepository;

    @Mock
    ConversationRepository conversationRepository;

    @Test
    @DisplayName("대화 내용 정상 조회 테스트")
    void getMessages_success() {
        //given
        Long conversationId = 1L;
        Conversation conversation = Conversation.builder().id(conversationId).build();
        Message message1 = Message.builder().build();
        Message message2 = Message.builder().build();

        when(conversationRepository.findById(anyLong())).thenReturn(Optional.of(conversation));
        when(messageRepository.findByConversationOrderByCreatedAtAsc(any(Conversation.class))).thenReturn(
                List.of(message1, message2));

        //when
        MessagesDto messagesDto = messageService.getMessages(conversationId);

        //then
        assertThat(messagesDto.getMessageDtos().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("존재하지 않는 채팅방일 경우 예외 발생")
    void getMessages_fail_when_conversation_not_found() {
        //given
        Long conversationId = 1L;

        when(conversationRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> messageService.getMessages(conversationId))
                .isInstanceOf(ChatException.class)
                .hasMessage(CONVERSATION_NOT_FOUND.getMessage());

        verify(messageRepository, never()).findByConversationOrderByCreatedAtAsc(any(Conversation.class));
    }
}