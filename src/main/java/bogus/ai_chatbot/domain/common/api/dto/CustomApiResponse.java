package bogus.ai_chatbot.domain.common.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomApiResponse<T> {

    @Schema(description = "응답 메시지", example = "정상적으로 응답이 반환되었습니다.")
    private String message;
    @Schema(description = "응답 데이터")
    private T data;

    public static <T> CustomApiResponse<T> from(String message) {
        return new CustomApiResponse<>(message, null);
    }

    public static <T> CustomApiResponse<T> of(String message, T data) {
        return new CustomApiResponse<>(message, data);
    }
}
