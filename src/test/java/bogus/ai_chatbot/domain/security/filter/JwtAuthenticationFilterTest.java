package bogus.ai_chatbot.domain.security.filter;

import static bogus.ai_chatbot.domain.exception.error.ErrorCode.*;
import static org.mockito.Mockito.*;

import bogus.ai_chatbot.domain.exception.error.ErrorCode;
import bogus.ai_chatbot.domain.exception.exception.AuthException;
import bogus.ai_chatbot.domain.jwt.util.JwtUtil;
import bogus.ai_chatbot.domain.security.properties.PermitPaths;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.List;
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
import org.springframework.util.AntPathMatcher;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @InjectMocks
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    JwtUtil jwtUtil;

    @Mock
    PermitPaths permitPaths;

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
    @DisplayName("토큰 인증 성공 테스트")
    void testMethod() throws ServletException, IOException {
        //given
        Long userId = 1L;
        String accessToken = "accessToken";
        String permitUri = "/permit/uri";

        request.setRequestURI("/need/authentication");
        request.addHeader("Authorization", "Bearer " + accessToken);

        when(permitPaths.getPaths()).thenReturn(List.of(permitUri));
        doNothing().when(jwtUtil).validateToken(anyString());
        doNothing().when(jwtUtil).validateAccessCategory(anyString());
        when(jwtUtil.getId(anyString())).thenReturn(userId);

        //when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //then
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Security Filter 통과 URI일 경우 Jwt 인증 필터 통과")
    void JwtAuthenticationFilter_pass_when_permit_uri() throws ServletException, IOException {
        //given
        String permitUri = "/permit/uri";
        request.setRequestURI(permitUri);

        when(permitPaths.getPaths()).thenReturn(List.of(permitUri));

        //when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //then
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtUtil, never()).validateToken(anyString());
        verify(jwtUtil, never()).validateAccessCategory(anyString());
    }

    @Test
    @DisplayName("Authorization 헤더가 존재하지 않을 경우 예외 발생")
    void throw_exception_when_header_null() throws ServletException, IOException {
        //given
        String permitUri = "/permit/uri";

        request.setRequestURI("/need/authentication");

        when(permitPaths.getPaths()).thenReturn(List.of(permitUri));

        //when & then
        Assertions.assertThatThrownBy(() -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain))
                .isInstanceOf(AuthException.class)
                .hasMessage(TOKEN_NULL.getMessage());

        verify(jwtUtil, never()).validateToken(anyString());
        verify(jwtUtil, never()).validateAccessCategory(anyString());
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("Authorization 헤더가 Bearer로 시작하지 않을 경우 예외 발생")
    void throw_exception_when_header_not_start_Bearer() throws ServletException, IOException {
        //given
        String accessToken = "accessToken";
        String permitUri = "/permit/uri";

        request.setRequestURI("/need/authentication");
        request.addHeader("Authorization", "NotBearer " + accessToken);

        when(permitPaths.getPaths()).thenReturn(List.of(permitUri));

        //when & then
        Assertions.assertThatThrownBy(() -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain))
                .isInstanceOf(AuthException.class)
                .hasMessage(TOKEN_NULL.getMessage());

        verify(jwtUtil, never()).validateToken(anyString());
        verify(jwtUtil, never()).validateAccessCategory(anyString());
        verify(filterChain, never()).doFilter(request, response);
    }
}