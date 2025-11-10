package bogus.ai_chatbot.domain.email.service;

import bogus.ai_chatbot.domain.email.dto.EmailDto;
import bogus.ai_chatbot.domain.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final MemberRepository memberRepository;

    public void sendVerificationCode(EmailDto emailDto) throws MessagingException {
        String email = emailDto.getEmail();

        validateDuplicateEmail(email);

        int code = createVerificationCode();
        MimeMessage emailForm = createEmailForm(email, code);

        javaMailSender.send(emailForm);
    }

    private void validateDuplicateEmail(String email) {
        if (existSameEmail(email)) {
            throw new RuntimeException("이미 해당 이메일로 가입한 이력이 있습니다.");
        }
    }

    private boolean existSameEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    private int createVerificationCode() {
        return new Random().nextInt(900000) + 100000;
    }

    private MimeMessage createEmailForm(String email, int code) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("AI ChatBot 인증 코드");

        String htmlContent = "<html><body>" +
                "<h3>AI ChatBot 인증 코드</h3>" +
                "<p><strong>" + code + "</strong></p>" +
                "</body></html>";

        helper.setText(htmlContent, true);

        return message;
    }
}
