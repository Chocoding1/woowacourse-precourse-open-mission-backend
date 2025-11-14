package bogus.ai_chatbot.domain.exception.error.dto;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ErrorResponse {

    private HttpStatus status;
    private String errorCode;
    private String errorMessage;
    private Map<String, String> errorFields;
    private String redirectUri;

    public static ErrorResponse of(HttpStatus status, String errorCode, String errorMessage) {
        return ErrorResponse.builder()
                .status(status)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }

    public static ErrorResponse ofFieldErrors(HttpStatus status, String errorCode, String errorMessage,
                                              Map<String, String> errorFields) {
        return ErrorResponse.builder()
                .status(status)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .errorFields(errorFields)
                .build();
    }

    public static ErrorResponse ofRedirectUri(HttpStatus status, String errorCode, String errorMessage,
                                              String redirectUri) {
        return ErrorResponse.builder()
                .status(status)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .redirectUri(redirectUri)
                .build();
    }
}
