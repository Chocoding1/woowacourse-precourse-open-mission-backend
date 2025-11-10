package bogus.ai_chatbot.domain.chat.service;

import bogus.ai_chatbot.domain.openai.dto.OpenAiResponse;
import bogus.ai_chatbot.domain.openai.service.OpenAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiClient openAiClient;

    public String getResponseMessage(String requestMessage) {
        OpenAiResponse openAiResponse = openAiClient.getAiResponse(requestMessage);

        return openAiResponse.getChoices().getFirst().getMessage().getContent();
    }
}
