package bogus.ai_chatbot.domain.chat.repository;

import bogus.ai_chatbot.domain.chat.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
