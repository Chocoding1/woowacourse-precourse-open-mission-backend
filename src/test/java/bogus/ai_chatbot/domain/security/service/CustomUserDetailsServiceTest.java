package bogus.ai_chatbot.domain.security.service;

import static bogus.ai_chatbot.domain.common.exception.error.ErrorCode.MEMBER_NOT_FOUND;

import bogus.ai_chatbot.domain.common.exception.exception.BusinessException;
import bogus.ai_chatbot.domain.member.repository.MemberRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @InjectMocks
    CustomUserDetailsService customUserDetailsService;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("해당 이메일로 가입된 유저가 존재하지 않을 시 예외 발생")
    void loadUserByUsername_fail_() {
        //given
        String email = "email@naver.com";
        Mockito.when(memberRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        //when & then
        Assertions.assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(email))
                .isInstanceOf(BusinessException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
    }
}