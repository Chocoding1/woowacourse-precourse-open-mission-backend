package bogus.ai_chatbot.domain.exception.exception;

import bogus.ai_chatbot.domain.exception.error.ErrorCode;

public class AuthException extends CustomException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
