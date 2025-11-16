package bogus.ai_chatbot.domain.chat.repository;

import bogus.ai_chatbot.domain.chat.entity.Conversation;
import bogus.ai_chatbot.domain.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> findByMemberOrderByModifiedAtDesc(Member member);
}
