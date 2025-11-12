package bogus.ai_chatbot.domain.exception.exception;

import bogus.ai_chatbot.domain.exception.error.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
