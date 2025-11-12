package bogus.ai_chatbot.domain.security.filter;

import static bogus.ai_chatbot.domain.exception.error.ErrorCode.*;
import static org.mockito.Mockito.*;

import bogus.ai_chatbot.domain.exception.exception.AuthException;
import bogus.ai_chatbot.domain.jwt.util.JwtUtil;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class CustomLogoutFilterTest {

    @InjectMocks
    CustomLogoutFilter customLogoutFilter;

    @Mock
    JwtUtil jwtUtil;

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @Mock
    MockFilterChain filterChain;

    @BeforeEach
    void init() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("정상 로그아웃 테스트")
    void logout_success() throws ServletException, IOException {
        //given
        String refreshToken = "refreshToken";

        request.setRequestURI("/logout");
        request.setMethod("POST");
        request.addHeader("Authorization-Refresh", refreshToken);

        doNothing().when(jwtUtil).validateTokenSame(anyString());
        doNothing().when(jwtUtil).validateRefreshCategory(anyString());
        doNothing().when(jwtUtil).deleteRefreshToken(anyString());

        //when
        customLogoutFilter.doFilter(request, response, filterChain);

        //then
        verify(jwtUtil, times(1)).deleteRefreshToken(anyString());
        Assertions.assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    @DisplayName("requestURI가 /logout이 아닐 경우 필터 통과")
    void CustomLogoutFilter_pass_when_requestURI_not_logout() throws ServletException, IOException {
        //given
        request.setRequestURI("/not-logout");

        //when
        customLogoutFilter.doFilter(request, response, filterChain);

        //then
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtUtil, never()).validateTokenSame(anyString());
        verify(jwtUtil, never()).validateRefreshCategory(anyString());
        verify(jwtUtil, never()).deleteRefreshToken(anyString());

    }

    @Test
    @DisplayName("requestURI가 /logout인데, requestMethod가 POST가 아닐 경우 예외 발생")
    void throw_exception_when_requestURI_is_logout_and_method_not_POST() {
        //given
        request.setRequestURI("/logout");
        request.setMethod("GET");

        //when & then
        Assertions.assertThatThrownBy(() -> customLogoutFilter.doFilter(request, response, filterChain))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_METHOD.getMessage());

        verify(jwtUtil, never()).validateTokenSame(anyString());
        verify(jwtUtil, never()).validateRefreshCategory(anyString());
        verify(jwtUtil, never()).deleteRefreshToken(anyString());
    }


    @Test
    @DisplayName("헤더에 refreshToken이 존재하지 않을 경우 예외 발생")
    void throw_exception_when_refreshToken_null() {
        //given
        request.setRequestURI("/logout");
        request.setMethod("POST");

        //when & then
        Assertions.assertThatThrownBy(() -> customLogoutFilter.doFilter(request, response, filterChain))
                .isInstanceOf(AuthException.class)
                .hasMessage(TOKEN_NULL.getMessage());

        verify(jwtUtil, never()).validateTokenSame(anyString());
        verify(jwtUtil, never()).validateRefreshCategory(anyString());
        verify(jwtUtil, never()).deleteRefreshToken(anyString());
    }
}