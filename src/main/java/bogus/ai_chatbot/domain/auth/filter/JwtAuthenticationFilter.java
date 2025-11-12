package bogus.ai_chatbot.domain.auth.filter;

import static bogus.ai_chatbot.domain.exception.error.ErrorCode.TOKEN_NULL;

import bogus.ai_chatbot.domain.auth.dto.CustomUserDetails;
import bogus.ai_chatbot.domain.exception.CustomAuthException;
import bogus.ai_chatbot.domain.jwt.util.JwtUtil;
import bogus.ai_chatbot.domain.member.dto.MemberSessionDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("JwtAuthenticationFilter -> doFilterInternal");

        String accessHeader = request.getHeader("Authorization");
        if (accessHeader == null || !accessHeader.startsWith("Bearer ")) {
            throw new CustomAuthException(TOKEN_NULL);
        }

        String accessToken = accessHeader.split(" ")[1];
        jwtUtil.validateToken(accessToken);
        jwtUtil.validateAccessCategory(accessToken);

        Long id = jwtUtil.getId(accessToken);
        saveAuthentication(id);

        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(Long id) {
        MemberSessionDto memberSessionDto = MemberSessionDto.builder()
                .id(id)
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(memberSessionDto);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
