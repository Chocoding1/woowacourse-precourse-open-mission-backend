package bogus.ai_chatbot.domain.chat.service;

import static bogus.ai_chatbot.domain.common.exception.error.ErrorCode.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import bogus.ai_chatbot.domain.chat.dto.ConversationsDto;
import bogus.ai_chatbot.domain.chat.entity.Conversation;
import bogus.ai_chatbot.domain.chat.repository.ConversationRepository;
import bogus.ai_chatbot.domain.common.exception.exception.ChatException;
import bogus.ai_chatbot.domain.member.entity.Member;
import bogus.ai_chatbot.domain.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
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
    @DisplayName("채팅방 정상 저장 테스트")
    void addConversation_success() {
        //given
        Long memberId = 1L;
        String prompt = "prompt";
        Member member = Member.builder()
                .id(memberId)
                .build();

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        //when
        conversationService.addConversation(memberId, prompt);

        //then
        Mockito.verify(conversationRepository, timeout(1)).save(any(Conversation.class));
    }

    @Test
    @DisplayName("회원이 존재하지 않을 경우 예외 발생")
    void addConversation_fail_when_member_not_found() {
        //given
        Long memberId = 1L;
        String prompt = "prompt";

        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> conversationService.addConversation(memberId, prompt))
                .isInstanceOf(ChatException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());

        verify(conversationRepository, never()).save(any(Conversation.class));
    }

    @Test
    @DisplayName("채팅방 목록 조회 성공 테스트")
    void getConversations_success() {
        //given
        Long memberId = 1L;
        Member member = Member.builder().id(memberId).build();

        Conversation conversation100 = Conversation.builder().id(100L).title("title_100").build();
        Conversation conversation200 = Conversation.builder().id(200L).title("title_200").build();
        List<Conversation> conversations = List.of(conversation100, conversation200);

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(conversationRepository.findByMember(any(Member.class))).thenReturn(conversations);

        //when
        ConversationsDto conversationsDto = conversationService.getConversations(memberId);

        //then
        assertThat(conversationsDto.getConversationDtos().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("회원이 존재하지 않을 경우 예외 발생")
    void getConversations_fail_when_member_not_found() {
        //given
        Long memberId = 1L;

        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> conversationService.getConversations(memberId))
                .isInstanceOf(ChatException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());

        verify(conversationRepository, never()).findByMember(any(Member.class));
    }
}