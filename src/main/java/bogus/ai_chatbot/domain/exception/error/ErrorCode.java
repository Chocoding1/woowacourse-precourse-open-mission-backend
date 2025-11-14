package bogus.ai_chatbot.domain.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // USER
    EMAIL_NOT_VERIFIED("EMAIL_NOT_VERIFIED", HttpStatus.BAD_REQUEST, "이메일 인증이 되지 않았습니다."),
    MEMBER_NOT_FOUND("MEMBER_NOT_FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    LOGIN_FAILED("LOGIN_FAILED", HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 일치하지 않습니다."),
    INVALID_LOGIN_FORM("INVALID_LOGIN_FOMR", HttpStatus.BAD_REQUEST, "로그인 정보가 유효하지 않습니다."),

    // CHAT
    CONVERSATION_NOT_FOUND("CONVERSATION_NOT_FOUND", HttpStatus.BAD_REQUEST, "존재하지 않는 대화입니다."),

    // OPEN_AI
    OPENAI_REQUEST_FAILED("OPENAI_REQUEST_FAILED", HttpStatus.INTERNAL_SERVER_ERROR, "OpenAI API 호출 과정 중 오류가 발생했습니다."),

    // JWT
    TOKEN_EXPIRED("TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN("INVALID_TOKEN", HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    TOKEN_NULL("TOKEN_NULL", HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    TOKEN_NOT_SAVED("TOKEN_NOT_SAVED", HttpStatus.UNAUTHORIZED, "토큰이 저장되어 있지 않습니다."),

    // EMAIL
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    INVALID_EMAIL_CODE("INVALID_EMAIL_CODE", HttpStatus.BAD_REQUEST, "이메일 인증 코드가 일치하지 않습니다."),
    EMAIL_CODE_NULL("EMAIL_CODE_NULL", HttpStatus.BAD_REQUEST, "해당 이메일의 인증 번호가 존재하지 않습니다."),

    // REQUEST
    INVALID_URI("INVALID_URI", HttpStatus.BAD_REQUEST, "잘못된 URI 요청입니다."),
    INVALID_METHOD("INVALID_METHOD", HttpStatus.METHOD_NOT_ALLOWED, "잘못된 요청 메서드입니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
