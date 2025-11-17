package bogus.ai_chatbot.domain.chat.service;

import static bogus.ai_chatbot.domain.common.exception.error.ErrorCode.MEMBER_NOT_FOUND;

import bogus.ai_chatbot.domain.chat.dto.ConversationDto;
import bogus.ai_chatbot.domain.chat.dto.ConversationsDto;
import bogus.ai_chatbot.domain.chat.entity.Conversation;
import bogus.ai_chatbot.domain.chat.repository.ConversationRepository;
import bogus.ai_chatbot.domain.common.exception.exception.ChatException;
import bogus.ai_chatbot.domain.member.entity.Member;
import bogus.ai_chatbot.domain.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final MemberRepository memberRepository;

    public Long addConversation(Long memberId, String prompt) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ChatException(MEMBER_NOT_FOUND));

        Conversation conversation = Conversation.builder()
                .title(prompt)
                .member(member)
                .build();

        conversationRepository.save(conversation);

        return conversation.getId();
    }

    public ConversationsDto getConversations(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ChatException(MEMBER_NOT_FOUND));

        List<ConversationDto> conversationDtos = conversationRepository.findByMemberOrderByModifiedAtDesc(member).stream()
                .map(conversation -> new ConversationDto((conversation.getId()), conversation.getTitle()))
                .toList();

        return new ConversationsDto(conversationDtos);
    }
}
