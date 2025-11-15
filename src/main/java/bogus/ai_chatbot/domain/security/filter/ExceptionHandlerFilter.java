package bogus.ai_chatbot.domain.security.filter;

import bogus.ai_chatbot.domain.common.exception.error.ErrorCode;
import bogus.ai_chatbot.domain.common.exception.error.dto.ErrorResponse;
import bogus.ai_chatbot.domain.common.exception.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            log.info("ExceptionHandlerFilter");
            sendErrorResponse(response, e);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, CustomException e) throws IOException {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getStatus(), errorCode.getCode(),
                errorCode.getMessage());

        ObjectMapper om = new ObjectMapper();
        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(om.writeValueAsString(errorResponse));
    }
}
