package bogus.ai_chatbot.domain.exception.exception;

import bogus.ai_chatbot.domain.exception.error.ErrorCode;

public class BusinessException extends CustomException{

    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
