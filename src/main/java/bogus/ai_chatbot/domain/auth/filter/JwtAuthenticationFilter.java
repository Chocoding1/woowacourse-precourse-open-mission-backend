package bogus.ai_chatbot.domain.auth.filter;

import bogus.ai_chatbot.domain.auth.dto.CustomUserDetails;
import bogus.ai_chatbot.domain.jwt.util.JwtUtil;
import bogus.ai_chatbot.domain.member.dto.MemberSessionDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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
            throw new RuntimeException("토큰이 존재하지 않습니다.");
        }

        String accessToken = accessHeader.split(" ")[1];

        try {
            jwtUtil.validateToken(accessToken);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

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
