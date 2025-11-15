package bogus.ai_chatbot.domain.chat.service;

import static bogus.ai_chatbot.domain.common.exception.error.ErrorCode.MEMBER_NOT_FOUND;

import bogus.ai_chatbot.domain.chat.entity.Conversation;
import bogus.ai_chatbot.domain.chat.repository.ConversationRepository;
import bogus.ai_chatbot.domain.common.exception.exception.ChatException;
import bogus.ai_chatbot.domain.member.entity.Member;
import bogus.ai_chatbot.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final MemberRepository memberRepository;

    public Long addConversation(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ChatException(MEMBER_NOT_FOUND));

        Conversation conversation = Conversation.builder()
                .title("tempTitle")
                .member(member)
                .build();

        conversationRepository.save(conversation);

        return conversation.getId();
    }
}
