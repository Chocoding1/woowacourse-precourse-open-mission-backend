package bogus.ai_chatbot.domain.common.exception.exception;

import bogus.ai_chatbot.domain.common.exception.error.ErrorCode;

public class AuthException extends CustomException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
