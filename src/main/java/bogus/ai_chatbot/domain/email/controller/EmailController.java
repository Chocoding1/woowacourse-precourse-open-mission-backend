package bogus.ai_chatbot.domain.email.controller;

import bogus.ai_chatbot.domain.email.dto.EmailDto;
import bogus.ai_chatbot.domain.email.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emails")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestBody EmailDto emailDto) throws MessagingException {
        emailService.sendAuthCode(emailDto);

        return ResponseEntity.ok("인증번호 전송 완료");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody EmailDto emailDto) {
        emailService.verifyAuthCode(emailDto);

        return ResponseEntity.ok("이메일 인증 성공");
    }
}
