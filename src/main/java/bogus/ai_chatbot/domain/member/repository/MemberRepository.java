package bogus.ai_chatbot.domain.member.repository;

import bogus.ai_chatbot.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);
}
