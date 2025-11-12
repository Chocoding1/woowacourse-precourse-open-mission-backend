package bogus.ai_chatbot.domain.security.service;

import static bogus.ai_chatbot.domain.exception.error.ErrorCode.MEMBER_NOT_FOUND;

import bogus.ai_chatbot.domain.security.dto.CustomUserDetails;
import bogus.ai_chatbot.domain.exception.exception.BusinessException;
import bogus.ai_chatbot.domain.member.dto.MemberSessionDto;
import bogus.ai_chatbot.domain.member.entity.Member;
import bogus.ai_chatbot.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("CustomUserDetailsService -> loadUserByUsername");

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        return new CustomUserDetails(MemberSessionDto.fromEntity(member));
    }
}
