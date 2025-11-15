package bogus.ai_chatbot.domain.openai.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import bogus.ai_chatbot.domain.common.exception.exception.BusinessException;
import bogus.ai_chatbot.domain.openai.dto.OpenAiRequest;
import bogus.ai_chatbot.domain.openai.dto.OpenAiResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class OpenAiClientTest {

    OpenAiClient openAiClient;

    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    void init() {
        String apiUrl = "http://test-api.com";
        String model = "gtp-test";

        openAiClient = new OpenAiClient(restTemplate, apiUrl, model);
    }

    @Test
    @DisplayName("OpenAI API 요청 실패 시 예외 발생")
    void testMethod() {
        //given
        String prompt = "prompt";
        ResponseEntity<OpenAiResponse> AiResponse = new ResponseEntity<>(null,
                HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.postForEntity(anyString(), any(OpenAiRequest.class), any(Class.class))).thenReturn(
                AiResponse);

        //when & then
        Assertions.assertThatThrownBy(() -> openAiClient.getAiResponse(prompt))
                .isInstanceOf(BusinessException.class)
                .hasMessage(OPENAI_REQUEST_FAILED.getMessage());
    }
}