package bogus.ai_chatbot.domain.common.exception.exception;

import bogus.ai_chatbot.domain.common.exception.error.ErrorCode;

public class ChatException extends CustomException{

    public ChatException(ErrorCode errorCode) {
        super(errorCode);
    }
}
