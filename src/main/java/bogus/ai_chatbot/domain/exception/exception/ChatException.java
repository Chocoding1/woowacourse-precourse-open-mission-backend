package bogus.ai_chatbot.domain.exception.exception;

import bogus.ai_chatbot.domain.exception.error.ErrorCode;

public class ChatException extends CustomException{

    public ChatException(ErrorCode errorCode) {
        super(errorCode);
    }
}
