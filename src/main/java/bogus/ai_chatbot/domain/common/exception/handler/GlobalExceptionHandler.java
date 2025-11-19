package bogus.ai_chatbot.domain.common.exception.handler;

import bogus.ai_chatbot.domain.common.exception.error.ErrorCode;
import bogus.ai_chatbot.domain.common.exception.error.dto.ErrorResponse;
import bogus.ai_chatbot.domain.common.exception.exception.AuthException;
import bogus.ai_chatbot.domain.common.exception.exception.CustomException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.info("GlobalExceptionHandler");
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException e) {
        log.info("GlobalExceptionHandler");
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.info("GlobalExceptionHandler");
        Map<String, String> errorFields = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error ->
                errorFields.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.ofFieldErrors(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "입력값이 유효하지 않습니다.", errorFields));
    }
}
