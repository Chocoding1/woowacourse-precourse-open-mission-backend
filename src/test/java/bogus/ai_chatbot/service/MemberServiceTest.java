package bogus.ai_chatbot.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import bogus.ai_chatbot.domain.member.entity.Member;
import bogus.ai_chatbot.domain.member.dto.MemberJoinDto;
import bogus.ai_chatbot.domain.member.repository.MemberRepository;
import bogus.ai_chatbot.domain.member.service.MemberService;
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

    @Test
    @DisplayName("정상 회원가입 테스트")
    void join_success() {
        //given
        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                .email("email")
                .password("password")
                .name("name")
                .build();

        when(memberRepository.existsByEmail(anyString())).thenReturn(false);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(anyString());

        //when
        memberService.join(memberJoinDto);

        //then
        verify(memberRepository, times(1)).existsByEmail(anyString());
        verify(bCryptPasswordEncoder, times(1)).encode(anyString());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("이미 존재하는 이메일일 경우 예외 발생")
    void join_fail_when_duplicate_email() {
        //given
        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                .email("duplicateEmail")
                .password("password")
                .name("name")
                .build();

        when(memberRepository.existsByEmail(anyString())).thenReturn(true);

        //when & then
        assertThatThrownBy(() -> memberService.join(memberJoinDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 해당 이메일로 가입한 이력이 있습니다.");

        verify(bCryptPasswordEncoder, never()).encode(anyString());
        verify(memberRepository, never()).save(any(Member.class));
    }
}