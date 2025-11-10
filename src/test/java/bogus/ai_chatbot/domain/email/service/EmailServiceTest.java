package bogus.ai_chatbot.domain.email.service;

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
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    EmailService emailService;

    @Mock
    JavaMailSender javaMailSender;
    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("인증 코드 전송 성공 테스트")
    void testMethod() throws MessagingException {
        //given
        EmailDto emailDto = EmailDto.builder()
                .email("email")
                .build();
        MimeMessage mockMessage = mock(MimeMessage.class);

        when(memberRepository.existsByEmail(anyString())).thenReturn(false);
        when(javaMailSender.createMimeMessage()).thenReturn(mockMessage);
        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        //when
        emailService.sendVerificationCode(emailDto);

        //then
        verify(memberRepository, times(1)).existsByEmail(anyString());
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("이미 가입된 이메일일 경우 예외 발생")
    void sendVerificationCode_fail_when_duplicate_email() {
        //given
        EmailDto emailDto = EmailDto.builder()
                .email("duplicateEmail")
                .build();

        when(memberRepository.existsByEmail(anyString())).thenReturn(true);

        //when & then
        assertThatThrownBy(() -> emailService.sendVerificationCode(emailDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 해당 이메일로 가입한 이력이 있습니다.");

        verify(javaMailSender, never()).createMimeMessage();
        verify(javaMailSender, never()).send(any(MimeMessage.class));
    }
}