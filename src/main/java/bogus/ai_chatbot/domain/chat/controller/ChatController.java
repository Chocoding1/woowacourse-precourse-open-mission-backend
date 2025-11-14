package bogus.ai_chatbot.domain.chat.controller;

import static bogus.ai_chatbot.domain.exception.error.ErrorCode.INVALID_URI;

import bogus.ai_chatbot.domain.chat.dto.ChatRequest;
import bogus.ai_chatbot.domain.chat.dto.ChatResponse;
import bogus.ai_chatbot.domain.chat.service.ChatService;
import bogus.ai_chatbot.domain.chat.service.ConversationService;
import bogus.ai_chatbot.domain.exception.exception.ChatException;
import bogus.ai_chatbot.domain.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final ConversationService conversationService;

    @PostMapping
    public ResponseEntity<ChatResponse> chatInNewConversation(@RequestBody ChatRequest chatRequest,
                                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("newChat");

        if (customUserDetails == null) {
            // 로그인을 안 한 경우 : 단순 message만으로 api 요청 후 응답
            String responseMessage = chatService.getResponseMessageWhenGuest(chatRequest.getPrompt());

            return ResponseEntity.ok(ChatResponse.from(responseMessage));
        }

        // 로그인을 한 경우 + 새 채팅 : memberId 가져가서 conversation 저장하고, api 요청 후 응답과 함께 conversationId로 redirect 요청
        Long memberId = customUserDetails.getId();
        Long conversationId = conversationService.addConversation(memberId);

        String responseMessage = chatService.getResponseMessageWhenMember(conversationId, chatRequest.getPrompt());

        return ResponseEntity.ok(ChatResponse.of(responseMessage, "/chats/" + conversationId));
    }

    @PostMapping("/{conversationId}")
    public ResponseEntity<ChatResponse> chatInOldConversation(@PathVariable Long conversationId,
                                                              @RequestBody ChatRequest chatRequest,
                                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("oldChat");

        if (customUserDetails == null) {
            // 예외 발생 후 새로운 채팅 endpoint로 redirect
            throw new ChatException(INVALID_URI);
        }

        //로그인 한 경우 : conversationId 가져가서 api 요청, 응답하고 각 메시지 저장(memberId는 필요 X)
        String responseMessage = chatService.getResponseMessageWhenMember(conversationId, chatRequest.getPrompt());

        return ResponseEntity.ok(ChatResponse.from(responseMessage));
    }
}
