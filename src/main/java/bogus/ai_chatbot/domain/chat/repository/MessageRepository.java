package bogus.ai_chatbot.domain.chat.repository;

import bogus.ai_chatbot.domain.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
