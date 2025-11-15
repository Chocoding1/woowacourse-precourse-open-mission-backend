package bogus.ai_chatbot.domain.common.exception.exception;

import bogus.ai_chatbot.domain.common.exception.error.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
