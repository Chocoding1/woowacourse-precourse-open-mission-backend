package bogus.ai_chatbot.domain.member.dto;

import bogus.ai_chatbot.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberSessionDto {

    private String email;
    private String password;

    public static MemberSessionDto fromEntity(Member member) {
        return MemberSessionDto.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .build();
    }
}
