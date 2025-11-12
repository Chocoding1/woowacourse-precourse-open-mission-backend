package bogus.ai_chatbot.domain.security.dto;

import bogus.ai_chatbot.domain.member.dto.MemberSessionDto;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final MemberSessionDto memberSessionDto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return memberSessionDto.getPassword();
    }

    @Override
    public String getUsername() {
        return memberSessionDto.getEmail();
    }

    public Long getId() {
        return memberSessionDto.getId();
    }
}
