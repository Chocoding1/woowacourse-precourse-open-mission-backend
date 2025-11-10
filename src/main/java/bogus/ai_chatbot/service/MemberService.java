package bogus.ai_chatbot.service;

import bogus.ai_chatbot.domain.MemberJoinDto;
import bogus.ai_chatbot.repository.MemberRepository;
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

    public void join(MemberJoinDto memberJoinDto) {
        validateDuplicateEmail(memberJoinDto.getEmail());

        encodeRawPassword(memberJoinDto);

        memberRepository.save(memberJoinDto.toEntity());
    }

    private void validateDuplicateEmail(String email) {
        if (existSameEmail(email)) {
            throw new RuntimeException("이미 해당 이메일로 가입한 이력이 있습니다.");
        }
    }

    private boolean existSameEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    private void encodeRawPassword(MemberJoinDto memberJoinDto) {
        String encodedPassword = bCryptPasswordEncoder.encode(memberJoinDto.getPassword());
        memberJoinDto.setEncodedPassword(encodedPassword);
    }
}
