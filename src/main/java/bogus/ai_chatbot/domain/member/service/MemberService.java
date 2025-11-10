package bogus.ai_chatbot.domain.member.service;

import bogus.ai_chatbot.domain.email.service.EmailService;
import bogus.ai_chatbot.domain.member.dto.MemberJoinDto;
import bogus.ai_chatbot.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;

    public void join(MemberJoinDto memberJoinDto) {
        checkEmailVerified(memberJoinDto.getEmail());

        encodeRawPassword(memberJoinDto);

        memberRepository.save(memberJoinDto.toEntity());
    }

    private void checkEmailVerified(String email) {
        if (!emailService.isEmailVerified(email)) {
            throw new RuntimeException("이메일 인증이 되지 않았습니다.");
        }
    }

    private void encodeRawPassword(MemberJoinDto memberJoinDto) {
        String encodedPassword = bCryptPasswordEncoder.encode(memberJoinDto.getPassword());
        memberJoinDto.setEncodedPassword(encodedPassword);
    }
}
