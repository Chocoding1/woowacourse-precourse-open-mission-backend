package bogus.ai_chatbot.domain.openai.service;

import static bogus.ai_chatbot.domain.exception.error.ErrorCode.OPENAI_REQUEST_FAILED;

import bogus.ai_chatbot.domain.exception.exception.BusinessException;
import bogus.ai_chatbot.domain.openai.dto.OpenAiMessage;
import bogus.ai_chatbot.domain.openai.dto.OpenAiRequest;
import bogus.ai_chatbot.domain.openai.dto.OpenAiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class OpenAiClient {

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String model;

    public OpenAiClient(RestTemplate restTemplate,
                        @Value("${openai.api-url}") String apiUrl,
                        @Value("${openai.model}") String model) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.model = model;
    }

    public OpenAiResponse getAiResponse(String prompt) {
        OpenAiRequest openAiRequest = createOpenAiRequest(prompt);

        ResponseEntity<OpenAiResponse> AiResponse = restTemplate.postForEntity(
                apiUrl,
                openAiRequest,
                OpenAiResponse.class);

        if (!AiResponse.getStatusCode().is2xxSuccessful() || AiResponse.getBody() == null) {
            throw new BusinessException(OPENAI_REQUEST_FAILED);
        }

        return AiResponse.getBody();
    }

    private OpenAiRequest createOpenAiRequest(String prompt) {
        OpenAiMessage userMessage = new OpenAiMessage("user", prompt);

        List<OpenAiMessage> messages = List.of(userMessage);

        return new OpenAiRequest(model, messages);
    }
}
