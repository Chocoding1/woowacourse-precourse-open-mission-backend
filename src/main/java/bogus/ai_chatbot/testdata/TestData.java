package bogus.ai_chatbot.testdata;

import bogus.ai_chatbot.domain.member.entity.Member;
import bogus.ai_chatbot.domain.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestData {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void initTestData() {
        Member member = Member.builder()
                .email("email")
                .password(bCryptPasswordEncoder.encode("pwd"))
                .name("name")
                .build();

        memberRepository.save(member);
    }
}
