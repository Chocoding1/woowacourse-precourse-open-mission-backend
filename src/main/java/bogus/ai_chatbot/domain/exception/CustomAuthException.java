package bogus.ai_chatbot.domain.exception;

import bogus.ai_chatbot.domain.exception.error.ErrorCode;
import lombok.Getter;

@Getter
public class CustomAuthException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomAuthException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
