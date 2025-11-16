package bogus.ai_chatbot.domain.chat.service;

import static bogus.ai_chatbot.domain.common.exception.error.ErrorCode.CONVERSATION_NOT_FOUND;
import static org.mockito.Mockito.*;

import bogus.ai_chatbot.domain.chat.entity.Conversation;
import bogus.ai_chatbot.domain.chat.entity.Message;
import bogus.ai_chatbot.domain.chat.repository.ConversationRepository;
import bogus.ai_chatbot.domain.chat.repository.MessageRepository;
import bogus.ai_chatbot.domain.common.exception.exception.ChatException;
import bogus.ai_chatbot.domain.openai.dto.OpenAiMessage;
import bogus.ai_chatbot.domain.openai.dto.OpenAiResponse;
import bogus.ai_chatbot.domain.openai.dto.OpenAiResponse.Choice;
import bogus.ai_chatbot.domain.openai.service.OpenAiClient;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @InjectMocks
    ChatService chatService;

    @Mock
    OpenAiClient openAiClient;

    @Mock
    ConversationRepository conversationRepository;

    @Mock
    MessageRepository messageRepository;

    @Test
    @DisplayName("기존 대화방에 메시지 전송 및 응답 성공 테스트")
    void getResponseMessageWhenMember_success() {
        //given
        Long conversationId = 1L;
        String prompt = "prompt";
        Conversation conversation = Conversation.builder()
                .id(conversationId)
                .build();
        OpenAiResponse openAiResponse = getOpenAiResponse();
        List<Message> messages = List.of();

        when(conversationRepository.findById(anyLong())).thenReturn(Optional.of(conversation));
        when(openAiClient.getAiResponse(anyString())).thenReturn(openAiResponse);
        when(messageRepository.saveAll(anyList())).thenReturn(messages);

        //when
        chatService.getResponseMessageWhenMember(conversationId, prompt);

        //then
        verify(conversationRepository, times(1)).findById(anyLong());
        verify(openAiClient, times(1)).getAiResponse(anyString());
        verify(messageRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("기존 대화방에 메시지 전송 시 대화방이 존재하지 않으면 예외 발생")
    void testMethod() {
        //given
        Long conversationId = 1L;
        String prompt = "prompt";

        when(conversationRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when & then
        Assertions.assertThatThrownBy(() -> chatService.getResponseMessageWhenMember(conversationId, prompt))
                .isInstanceOf(ChatException.class)
                .hasMessage(CONVERSATION_NOT_FOUND.getMessage());

        verify(openAiClient, never()).getAiResponse(anyString());
        verify(messageRepository, never()).saveAll(anyList());
    }

    private static OpenAiResponse getOpenAiResponse() {
        OpenAiMessage openAiMessage = OpenAiMessage.builder().build();
        Choice choice = Choice.builder()
                .message(openAiMessage)
                .build();
        return OpenAiResponse.builder()
                .choices(List.of(choice))
                .build();
    }
}