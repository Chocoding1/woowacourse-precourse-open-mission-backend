package bogus.ai_chatbot.domain.member.dto;

import bogus.ai_chatbot.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberJoinDto {

    @Schema(description = "이메일", example = "email@naver.com")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @Schema(description = "비밀번호", example = "1234")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Schema(description = "이름", example = "보거스")
    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    public void setEncodedPassword(String encodedPassword) {
        password = encodedPassword;
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }
}
