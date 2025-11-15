package bogus.ai_chatbot.domain.common.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomApiResponse<T> {

    private String message;
    private T data;

    public static <T> CustomApiResponse<T> from(String message) {
        return new CustomApiResponse<>(message, null);
    }

    public static <T> CustomApiResponse<T> of(String message, T data) {
        return new CustomApiResponse<>(message, data);
    }
}
