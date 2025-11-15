package bogus.ai_chatbot.domain.member.service;

import static bogus.ai_chatbot.domain.common.exception.error.ErrorCode.EMAIL_NOT_VERIFIED;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import bogus.ai_chatbot.domain.email.service.EmailService;
import bogus.ai_chatbot.domain.common.exception.exception.BusinessException;
import bogus.ai_chatbot.domain.member.entity.Member;
import bogus.ai_chatbot.domain.member.dto.MemberJoinDto;
import bogus.ai_chatbot.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    EmailService emailService;

    @Test
    @DisplayName("정상 회원가입 테스트")
    void join_success() {
        //given
        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                .email("email")
                .password("password")
                .name("name")
                .build();

        when(emailService.isEmailVerified(anyString())).thenReturn(true);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(anyString());

        //when
        memberService.join(memberJoinDto);

        //then
        verify(emailService, times(1)).isEmailVerified(anyString());
        verify(bCryptPasswordEncoder, times(1)).encode(anyString());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("이메일 미인증 시 예외 발생")
    void checkVerifiedEmail_fail_when_unauthenticated_email() {
        //given
        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                .email("email")
                .password("password")
                .name("name")
                .build();

        when(emailService.isEmailVerified(anyString())).thenReturn(false);

        //when & then
        assertThatThrownBy(() -> memberService.join(memberJoinDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage(EMAIL_NOT_VERIFIED.getMessage());

        verify(emailService, times(1)).isEmailVerified(anyString());
        verify(bCryptPasswordEncoder, never()).encode(anyString());
        verify(memberRepository, never()).save(any(Member.class));
    }
}