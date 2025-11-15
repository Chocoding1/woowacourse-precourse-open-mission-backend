package bogus.ai_chatbot.domain.common.exception.exception;

import bogus.ai_chatbot.domain.common.exception.error.ErrorCode;

public class BusinessException extends CustomException{

    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
