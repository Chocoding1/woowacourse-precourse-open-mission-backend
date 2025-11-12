package bogus.ai_chatbot.domain.auth.filter;

import static bogus.ai_chatbot.domain.exception.error.ErrorCode.INVALID_LOGIN_FORM;
import static bogus.ai_chatbot.domain.exception.error.ErrorCode.LOGIN_FAILED;

import bogus.ai_chatbot.domain.auth.dto.CustomUserDetails;
import bogus.ai_chatbot.domain.exception.exception.AuthException;
import bogus.ai_chatbot.domain.exception.exception.BusinessException;
import bogus.ai_chatbot.domain.jwt.dto.JwtInfoDto;
import bogus.ai_chatbot.domain.jwt.util.JwtUtil;
import bogus.ai_chatbot.domain.member.dto.MemberLoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("LoginFilter -> attemptAuthentication");

        MemberLoginDto memberLoginDto = getMemberLoginDto(request);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                memberLoginDto.getEmail(), memberLoginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException {
        log.info("LoginFilter -> successfulAuthentication");

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        JwtInfoDto jwtInfoDto = jwtUtil.createJwt(customUserDetails.getId());

        setResponse(response, jwtInfoDto);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.info("LoginFilter -> unsuccessfulAuthentication");

        throw new AuthException(LOGIN_FAILED);
    }

    private static MemberLoginDto getMemberLoginDto(HttpServletRequest request) {
        ObjectMapper om = new ObjectMapper();
        try {
            return om.readValue(request.getInputStream(), MemberLoginDto.class);
        } catch (IOException e) {
            throw new BusinessException(INVALID_LOGIN_FORM);
        }
    }

    private static void setResponse(HttpServletResponse response, JwtInfoDto jwtInfoDto) throws IOException {
        response.setHeader("Authorization", "Bearer " + jwtInfoDto.getAccessToken());
        response.setHeader("Authorization-Refresh", jwtInfoDto.getRefreshToken());
        response.setStatus(200);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.getWriter().write("로그인 성공");
    }
}
