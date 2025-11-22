package bogus.ai_chatbot.domain.security.filter;

import static bogus.ai_chatbot.domain.common.exception.error.ErrorCode.INVALID_METHOD;
import static bogus.ai_chatbot.domain.common.exception.error.ErrorCode.TOKEN_NULL;

import bogus.ai_chatbot.domain.common.exception.exception.AuthException;
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

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info("requestURI : " + request.getRequestURI());

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
            throw new AuthException(INVALID_METHOD);
        }

        String refreshToken = request.getHeader("Authorization-Refresh");
        if (refreshToken == null) {
            throw new AuthException(TOKEN_NULL);
        }

        jwtUtil.validateTokenSame(refreshToken);
        jwtUtil.validateRefreshCategory(refreshToken);

        jwtUtil.deleteRefreshToken(refreshToken);

        setResponse(response);
    }

    private static void setResponse(HttpServletResponse response) throws IOException {
        response.setStatus(200);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.getWriter().write("로그아웃 완료");
    }
}
