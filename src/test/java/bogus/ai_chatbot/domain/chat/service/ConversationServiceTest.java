package bogus.ai_chatbot.domain.chat.service;

import static bogus.ai_chatbot.domain.exception.error.ErrorCode.*;
import static org.mockito.Mockito.*;

import bogus.ai_chatbot.domain.chat.entity.Conversation;
import bogus.ai_chatbot.domain.chat.repository.ConversationRepository;
import bogus.ai_chatbot.domain.exception.error.ErrorCode;
import bogus.ai_chatbot.domain.exception.exception.ChatException;
import bogus.ai_chatbot.domain.member.entity.Member;
import bogus.ai_chatbot.domain.member.repository.MemberRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConversationServiceTest {

    @InjectMocks
    ConversationService conversationService;

    @Mock
    ConversationRepository conversationRepository;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("대화방 정상 저장 테스트")
    void addConversation_success() {
        //given
        Long memberId = 1L;
        Member member = Member.builder()
                .id(memberId)
                .build();

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        //when
        conversationService.addConversation(memberId);

        //then
        Mockito.verify(conversationRepository, timeout(1)).save(any(Conversation.class));
    }

    @Test
    @DisplayName("회원이 존재하지 않을 경우 예외 발생")
    void testMethod() {
        //given
        Long memberId = 1L;

        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when & then
        Assertions.assertThatThrownBy(() -> conversationService.addConversation(memberId))
                .isInstanceOf(ChatException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());

        verify(conversationRepository, never()).save(any(Conversation.class));
    }
}