package bogus.ai_chatbot.domain.chat.controller;

import static bogus.ai_chatbot.domain.common.exception.error.ErrorCode.INVALID_URI;

import bogus.ai_chatbot.domain.chat.dto.ChatRequest;
import bogus.ai_chatbot.domain.chat.dto.ChatResponse;
import bogus.ai_chatbot.domain.chat.dto.ConversationsDto;
import bogus.ai_chatbot.domain.chat.service.ChatService;
import bogus.ai_chatbot.domain.chat.service.ConversationService;
import bogus.ai_chatbot.domain.common.api.dto.CustomApiResponse;
import bogus.ai_chatbot.domain.common.exception.exception.ChatException;
import bogus.ai_chatbot.domain.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Chat API", description = "채팅 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final ConversationService conversationService;

    @Operation(summary = "로그인한 사용자의 채팅방 목록 반환", description = "로그인한 사용자의 채팅방 목록(id, title) 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "AI 응답 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원")
    })
    @GetMapping("/conversations")
    public ResponseEntity<CustomApiResponse<ConversationsDto>> getConversations(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long memberId = customUserDetails.getId();

        ConversationsDto conversationsDto = conversationService.getConversations(memberId);

        return ResponseEntity.ok(CustomApiResponse.of("채팅방 목록 조회 성공", conversationsDto));
    }

    @Operation(summary = "새로운 채팅방에서 prompt 작성", description = "prompt를 전송하면 새로운 채팅방 만들어서 응답 반환 + 새로운 채팅방 URI로 redirect 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "AI 응답 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원 / 존재하지 않는 채팅방"),
            @ApiResponse(responseCode = "500", description = "OpenAI 내부 오류")
    })
    @PostMapping
    public ResponseEntity<CustomApiResponse<ChatResponse>> chatInNewConversation(@RequestBody ChatRequest chatRequest,
                                                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("newChat");

        if (customUserDetails == null) {
            // 로그인을 안 한 경우 : 단순 message만으로 api 요청 후 응답
            String responseMessage = chatService.getResponseMessageWhenGuest(chatRequest.getPrompt());

            return ResponseEntity.ok(CustomApiResponse.of("AI 응답 완료", ChatResponse.from(responseMessage)));
        }

        // 로그인을 한 경우 + 새 채팅 : memberId 가져가서 conversation 저장하고, api 요청 후 응답과 함께 conversationId로 redirect 요청
        Long memberId = customUserDetails.getId();
        Long conversationId = conversationService.addConversation(memberId, chatRequest.getPrompt());

        String responseMessage = chatService.getResponseMessageWhenMember(conversationId, chatRequest.getPrompt());

        return ResponseEntity.ok(
                CustomApiResponse.of("AI 응답 완료", ChatResponse.of(responseMessage, "/chats/" + conversationId)));
    }

    @Operation(summary = "기존 채팅방에서 prompt 작성", description = "prompt를 전송하면 응답 반환",
            parameters = {
                    @Parameter(name = "conversationId", description = "채팅방 ID",
                            required = true, in = ParameterIn.PATH)
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "AI 응답 성공"),
            @ApiResponse(responseCode = "400", description = "로그인하지 않은 유저가 채팅방 접근"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 채팅방 접근"),
            @ApiResponse(responseCode = "500", description = "OpenAI 내부 오류")
    })
    @PostMapping("/{conversationId}")
    public ResponseEntity<CustomApiResponse<ChatResponse>> chatInOldConversation(@PathVariable Long conversationId,
                                                                                 @RequestBody ChatRequest chatRequest,
                                                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("oldChat");

        if (customUserDetails == null) {
            // 예외 발생 후 새로운 채팅 endpoint로 redirect
            throw new ChatException(INVALID_URI);
        }

        //로그인 한 경우 : conversationId 가져가서 api 요청, 응답하고 각 메시지 저장(memberId는 필요 X)
        String responseMessage = chatService.getResponseMessageWhenMember(conversationId, chatRequest.getPrompt());

        return ResponseEntity.ok(CustomApiResponse.of("AI 응답 완료", ChatResponse.from(responseMessage)));
    }
}
