package bogus.ai_chatbot.domain.email.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bogus.ai_chatbot.domain.email.dto.EmailDto;
import bogus.ai_chatbot.domain.member.repository.MemberRepository;
import bogus.ai_chatbot.domain.redis.service.RedisService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    private static final String VERIFIED_MESSAGE = "verified";

    @InjectMocks
    EmailService emailService;

    @Mock
    JavaMailSender javaMailSender;
    @Mock
    MemberRepository memberRepository;
    @Mock
    RedisService redisService;

    @Test
    @DisplayName("인증 코드 전송 성공 테스트")
    void sendAuthCode_success() throws MessagingException {
        //given
        EmailDto emailDto = EmailDto.builder()
                .email("email@naver.com")
                .build();
        MimeMessage mockMessage = mock(MimeMessage.class);

        when(memberRepository.existsByEmail(anyString())).thenReturn(false);
        when(javaMailSender.createMimeMessage()).thenReturn(mockMessage);
        doNothing().when(javaMailSender).send(any(MimeMessage.class));
        doNothing().when(redisService).saveEmailAuthCode(anyString(), anyString());

        //when
        emailService.sendAuthCode(emailDto);

        //then
        verify(memberRepository, times(1)).existsByEmail(anyString());
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("이미 가입된 이메일일 경우 예외 발생")
    void sendAuthCode_fail_when_duplicate_email() {
        //given
        EmailDto emailDto = EmailDto.builder()
                .email("dupli@naver.com")
                .build();

        when(memberRepository.existsByEmail(anyString())).thenReturn(true);

        //when & then
        assertThatThrownBy(() -> emailService.sendAuthCode(emailDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 해당 이메일로 가입한 이력이 있습니다.");

        verify(javaMailSender, never()).createMimeMessage();
        verify(javaMailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("이메일 인증 번호 검증 성공 테스트")
    void verifyAuthCode_success() {
        //given
        String authCode = "authCode";
        EmailDto emailDto = EmailDto.builder()
                .email("email@naver.com")
                .authCode(authCode)
                .build();

        when(redisService.getEmailAuthCode(anyString())).thenReturn(authCode);
        doNothing().when(redisService).saveEmailAuthCode(anyString(), anyString());

        //when
        emailService.verifyAuthCode(emailDto);

        //then
        verify(redisService, times(1)).getEmailAuthCode(anyString());
        verify(redisService, times(1)).saveEmailAuthCode(anyString(), anyString());
    }

    @Test
    @DisplayName("해당 이메일의 인증 번호가 존재하지 않을 경우 예외 발생")
    void verifyAuthCode_fail_when_authCode_null() {
        //given
        EmailDto emailDto = EmailDto.builder()
                .email("email@naver.com")
                .authCode("authCode")
                .build();

        when(redisService.getEmailAuthCode(anyString())).thenReturn(null);

        //when & then
        assertThatThrownBy(() -> emailService.verifyAuthCode(emailDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("해당 이메일의 인증 번호가 존재하지 않습니다.");

        verify(redisService, times(1)).getEmailAuthCode(anyString());
        verify(redisService, never()).saveEmailAuthCode(anyString(), anyString());
    }

    @Test
    @DisplayName("인증 번호가 일치하지 경우 예외 발생")
    void verifyAuthCode_fail_when_authCode_not_equal() {
        //given
        String invalidAuthCode = "invalidAuthCode";
        EmailDto emailDto = EmailDto.builder()
                .email("email@naver.com")
                .authCode("authCode")
                .build();

        when(redisService.getEmailAuthCode(anyString())).thenReturn(invalidAuthCode);

        //when & then
        assertThatThrownBy(() -> emailService.verifyAuthCode(emailDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("인증 번호가 일치하지 않습니다.");

        verify(redisService, times(1)).getEmailAuthCode(anyString());
        verify(redisService, never()).saveEmailAuthCode(anyString(), anyString());
    }

    @Test
    @DisplayName("인증된 이메일일 경우 true 반환 테스트")
    void isEmailVerified_success() {
        //given
        String email = "email@naver.com";

        when(redisService.getEmailAuthCode(anyString())).thenReturn(VERIFIED_MESSAGE);

        //when
        boolean isVerified = emailService.isEmailVerified(email);

        //then
        assertThat(isVerified).isTrue();
    }

    @Test
    @DisplayName("인증되지 않은 이메일일 경우 false 반환 테스트")
    void isEmailVerified_fail_not_verified_email() {
        //given
        String email = "email@naver.com";
        String findMessage = "unVerified";

        when(redisService.getEmailAuthCode(anyString())).thenReturn(findMessage);

        //when
        boolean isVerified = emailService.isEmailVerified(email);

        //then
        Assertions.assertThat(isVerified).isFalse();
    }
}