package bogus.ai_chatbot.domain.email.service;

import static bogus.ai_chatbot.domain.common.exception.error.ErrorCode.DUPLICATE_EMAIL;
import static bogus.ai_chatbot.domain.common.exception.error.ErrorCode.EMAIL_CODE_NULL;
import static bogus.ai_chatbot.domain.common.exception.error.ErrorCode.INVALID_EMAIL_CODE;

import bogus.ai_chatbot.domain.email.dto.EmailDto;
import bogus.ai_chatbot.domain.common.exception.exception.BusinessException;
import bogus.ai_chatbot.domain.member.repository.MemberRepository;
import bogus.ai_chatbot.domain.redis.service.RedisService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final String VERIFIED_MESSAGE = "verified";

    private final JavaMailSender javaMailSender;
    private final MemberRepository memberRepository;
    private final RedisService redisService;

    public void sendAuthCode(EmailDto emailDto) throws MessagingException {
        String email = emailDto.getEmail();
        System.out.println(email);

        validateDuplicateEmail(email);

        String code = createAuthCode();
        MimeMessage emailForm = createEmailForm(email, code);

        javaMailSender.send(emailForm);

        redisService.saveEmailAuthCode(email, code);
    }

    public void verifyAuthCode(EmailDto emailDto) {
        String email = emailDto.getEmail();
        String findAuthCode = redisService.getEmailAuthCode(email);

        if (findAuthCode == null) {
            throw new BusinessException(EMAIL_CODE_NULL);
        }

        if (!emailDto.getAuthCode().equals(findAuthCode)) {
            throw new BusinessException(INVALID_EMAIL_CODE);
        }

        redisService.saveEmailAuthCode(email, VERIFIED_MESSAGE);
    }

    public boolean isEmailVerified(String email) {
        String verifiedMessage = redisService.getEmailAuthCode(email);

        return VERIFIED_MESSAGE.equals(verifiedMessage);
    }

    private void validateDuplicateEmail(String email) {
        if (existSameEmail(email)) {
            throw new BusinessException(DUPLICATE_EMAIL);
        }
    }

    private boolean existSameEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    private String createAuthCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    private MimeMessage createEmailForm(String email, String code) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("AI ChatBot 인증코드");

        String htmlContent = "<html><body>" +
                "<h3>AI ChatBot 인증 코드</h3>" +
                "<p><strong>" + code + "</strong></p>" +
                "</body></html>";

        helper.setText(htmlContent, true);

        return message;
    }
}
