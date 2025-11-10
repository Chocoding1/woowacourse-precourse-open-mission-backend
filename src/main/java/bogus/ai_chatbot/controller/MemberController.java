package bogus.ai_chatbot.controller;

import bogus.ai_chatbot.domain.MemberJoinDto;
import bogus.ai_chatbot.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<String> joinMember(@Validated @RequestBody MemberJoinDto memberJoinDto) {
        memberService.join(memberJoinDto);

        return ResponseEntity.status(201).body("회원가입 성공");
    }
}
