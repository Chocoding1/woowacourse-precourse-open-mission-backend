package bogus.ai_chatbot.domain.chat.repository;

import bogus.ai_chatbot.domain.chat.entity.Conversation;
import bogus.ai_chatbot.domain.chat.entity.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversationOrderByCreatedAtAsc(Conversation conversation);
}
