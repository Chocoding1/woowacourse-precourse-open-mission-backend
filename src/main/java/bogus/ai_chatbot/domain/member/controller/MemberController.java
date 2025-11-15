package bogus.ai_chatbot.domain.member.controller;

import bogus.ai_chatbot.domain.member.dto.MemberJoinDto;
import bogus.ai_chatbot.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member API", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "회원가입 정보를 파라미터로 보내면 회원가입 진행",
    responses = {
            @ApiResponse(responseCode = "201", description = "회원가입 완료"),
            @ApiResponse(responseCode = "400", description = "이메일 인증 미완료")
    })
    @PostMapping
    public ResponseEntity<String> joinMember(@Validated @RequestBody MemberJoinDto memberJoinDto) {
        memberService.join(memberJoinDto);

        return ResponseEntity.status(201).body("회원가입 성공");
    }
}
