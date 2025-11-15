package bogus.ai_chatbot.domain.jwt.controller;

import bogus.ai_chatbot.domain.jwt.dto.JwtInfoDto;
import bogus.ai_chatbot.domain.jwt.service.ReissueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "JWT API", description = "JWT 토큰 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/jwts")
public class ReissueController {

    private final ReissueService reissueService;

    @Operation(summary = "토큰 재발급", description = "Refresh Token을 파라미터로 보내면 Access Token, Refresh Token 재발급",
            responses = {
                    @ApiResponse(responseCode = "200", description = "토큰 재발급 완료"),
                    @ApiResponse(responseCode = "401", description = "인증 실패(토큰 없음 / 토큰 만료 / 유효하지 않은 토큰 / 저장되지 않은 토큰)"),
            })
    @PostMapping("/reissue")
    public ResponseEntity<String> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader("Authorization-Refresh");

        JwtInfoDto jwtInfoDto = reissueService.reissueToken(refreshToken);

        response.setHeader("Authorization", "Bearer " + jwtInfoDto.getAccessToken());
        response.setHeader("Authorization-Refresh", jwtInfoDto.getRefreshToken());

        return ResponseEntity.ok("토큰 재발급 완료");
    }
}
