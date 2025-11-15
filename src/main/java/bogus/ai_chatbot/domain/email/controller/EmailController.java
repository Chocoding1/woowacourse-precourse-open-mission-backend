package bogus.ai_chatbot.domain.email.controller;

import bogus.ai_chatbot.domain.email.dto.EmailDto;
import bogus.ai_chatbot.domain.email.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Email API", description = "이메일 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/emails")
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "이메일 인증코드 전송", description = "이메일을 파라미터로 보내면 해당 이메일로 인증코드 전송")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 인증코드 전송 완료"),
            @ApiResponse(responseCode = "409", description = "이미 가입된 이메일")
    })
    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestBody EmailDto emailDto) throws MessagingException {
        emailService.sendAuthCode(emailDto);

        return ResponseEntity.ok("인증코드 전송 완료");
    }

    @Operation(summary = "이메일 인증코드 검증", description = "이메일과 인증코드를 파라미터로 보내면 해당 이메일로 인증코드 검증")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 인증코드 검증 완료"),
            @ApiResponse(responseCode = "400", description = "일치하지 않는 인증코드"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 인증코드")
    })
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody EmailDto emailDto) {
        emailService.verifyAuthCode(emailDto);

        return ResponseEntity.ok("이메일 인증 성공");
    }
}
