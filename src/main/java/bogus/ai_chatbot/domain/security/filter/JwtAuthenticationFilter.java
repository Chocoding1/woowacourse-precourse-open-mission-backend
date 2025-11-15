package bogus.ai_chatbot.domain.security.filter;

import static bogus.ai_chatbot.domain.common.exception.error.ErrorCode.TOKEN_NULL;

import bogus.ai_chatbot.domain.security.dto.CustomUserDetails;
import bogus.ai_chatbot.domain.common.exception.exception.AuthException;
import bogus.ai_chatbot.domain.jwt.util.JwtUtil;
import bogus.ai_chatbot.domain.member.dto.MemberSessionDto;
import bogus.ai_chatbot.domain.security.properties.PermitPaths;
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
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final PermitPaths permitPaths;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("JwtAuthenticationFilter -> doFilterInternal");

        String requestURI = request.getRequestURI();
        if (isPermitPaths(requestURI)) {
            log.info("Permit Path : " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        String accessHeader = request.getHeader("Authorization");

        if (pathMatcher.match("/chats/**", requestURI) && accessHeader == null) {
            log.info("chats URI : " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        if (accessHeader == null || !accessHeader.startsWith("Bearer ")) {
            throw new AuthException(TOKEN_NULL);
        }

        String accessToken = accessHeader.split(" ")[1];
        jwtUtil.validateToken(accessToken);
        jwtUtil.validateAccessCategory(accessToken);

        Long id = jwtUtil.getId(accessToken);
        saveAuthentication(id);

        filterChain.doFilter(request, response);
    }

    private boolean isPermitPaths(String requestURI) {
        return permitPaths.getPaths().stream()
                .anyMatch(path -> pathMatcher.match(path, requestURI));
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
