package bogus.ai_chatbot.domain.chat.controller;

import bogus.ai_chatbot.domain.chat.dto.ChatRequest;
import bogus.ai_chatbot.domain.chat.dto.ChatResponse;
import bogus.ai_chatbot.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest chatRequest) {
        log.info("ChatRequest : " + chatRequest);
        String responseMessage = chatService.getResponseMessage(chatRequest.getMessage());

        return ResponseEntity.ok(ChatResponse.of(responseMessage));
    }
}
