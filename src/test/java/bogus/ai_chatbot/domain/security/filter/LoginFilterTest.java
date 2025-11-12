package bogus.ai_chatbot.domain.security.filter;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import bogus.ai_chatbot.domain.exception.error.ErrorCode;
import bogus.ai_chatbot.domain.exception.exception.AuthException;
import bogus.ai_chatbot.domain.exception.exception.BusinessException;
import bogus.ai_chatbot.domain.jwt.dto.JwtInfoDto;
import bogus.ai_chatbot.domain.jwt.util.JwtUtil;
import bogus.ai_chatbot.domain.member.dto.MemberLoginDto;
import bogus.ai_chatbot.domain.security.dto.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@ExtendWith(MockitoExtension.class)
class LoginFilterTest {

    @InjectMocks
    LoginFilter loginFilter;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtUtil jwtUtil;

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeEach
    void init() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("로그인 정보 json 파싱 성공 시 정상 반환 테스트")
    void attemptAuthentication_success() throws JsonProcessingException {
        //given
        MemberLoginDto memberLoginDto = MemberLoginDto.builder()
                .email("email@naver.com")
                .password("pwd")
                .build();
        String loginJson = new ObjectMapper().writeValueAsString(memberLoginDto);

        request.setContentType("application/json");
        request.setContent(loginJson.getBytes());

        //when
        loginFilter.attemptAuthentication(request, response);

        //then
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    @DisplayName("로그인 정보 json 파싱 실패 시 예외 발생")
    void attemptAuthentication_fail_when_invalid_json() {
        //given
        request.setContentType("application/json");
        request.setContent("invalidJson".getBytes());

        //when & then
        assertThatThrownBy(() -> loginFilter.attemptAuthentication(request, response))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INVALID_LOGIN_FORM.getMessage());

        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    @DisplayName("로그인 성공 시, 응답 헤더에 AT, RT 포함 테스트")
    void successfulAuthentication_then_contain_token() throws IOException {
        //given
        JwtInfoDto jwtInfoDto = JwtInfoDto.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(jwtUtil.createJwt(anyLong())).thenReturn(jwtInfoDto);

        //when
        loginFilter.successfulAuthentication(request, response, mock(FilterChain.class), authentication);

        //then
        assertThat(response.getHeader("Authorization")).isEqualTo("Bearer " + jwtInfoDto.getAccessToken());
        assertThat(response.getHeader("Authorization-Refresh")).isEqualTo(jwtInfoDto.getRefreshToken());
    }

    @Test
    @DisplayName("로그인 실패 시 예외 발생")
    void unsuccessfulAuthentication_then_throw_exception() {
        //when & then
        Assertions.assertThatThrownBy(() -> loginFilter.unsuccessfulAuthentication(request, response, mock(
                        AuthenticationException.class)))
                .isInstanceOf(AuthException.class)
                .hasMessage(ErrorCode.LOGIN_FAILED.getMessage());
    }
}