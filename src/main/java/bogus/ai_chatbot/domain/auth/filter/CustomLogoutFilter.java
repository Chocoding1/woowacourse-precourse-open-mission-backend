package bogus.ai_chatbot.domain.auth.filter;

import bogus.ai_chatbot.domain.jwt.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        log.info("CustomLogoutFilter -> doFilter");

        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (!requestURI.matches("^\\/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            throw new RuntimeException("잘못된 요청 메서드입니다.");
        }

        String refreshToken = request.getHeader("Authorization-Refresh");
        if (refreshToken == null) {
            throw new RuntimeException("토큰이 존재하지 않습니다.");
        }

        Long userId = jwtUtil.getId(refreshToken);
        String savedRefreshToken = jwtUtil.getRefreshToken(userId);

        if (!refreshToken.equals(savedRefreshToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refresh")) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        jwtUtil.deleteRefreshToken(userId);

        response.setStatus(200);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.getWriter().write("로그아웃 완료");
    }
}
