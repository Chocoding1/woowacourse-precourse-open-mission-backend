package bogus.ai_chatbot.domain.openai.service;

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
@RequiredArgsConstructor
@Slf4j
public class OpenAiClient {

    private final RestTemplate restTemplate;

    @Value("${openai.api-url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;

    public OpenAiResponse getAiResponse(String prompt) {
        OpenAiRequest openAiRequest = createOpenAiRequest(prompt);
        log.info("openAirequest : " + openAiRequest);

        ResponseEntity<OpenAiResponse> AiResponse = restTemplate.postForEntity(
                apiUrl,
                openAiRequest,
                OpenAiResponse.class);

        if (!AiResponse.getStatusCode().is2xxSuccessful() || AiResponse.getBody() == null) {
            throw new RuntimeException("OpenAI API 호출 실패");
        }

        log.info("OpenAiResponse : " + AiResponse.getBody());

        return AiResponse.getBody();
    }

    private OpenAiRequest createOpenAiRequest(String prompt) {
        log.info("model : " + model);
        OpenAiMessage userMessage = new OpenAiMessage("user", prompt);

        List<OpenAiMessage> messages = List.of(userMessage);

        return new OpenAiRequest(model, messages);
    }
}
