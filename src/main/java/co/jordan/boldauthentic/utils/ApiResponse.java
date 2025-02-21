package co.jordan.boldauthentic.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiResponse <T>{
    private boolean success;
    private String message;
    private T data;
    private int statusCode;

    public static <T> ApiResponse<T> success(String message, T data, HttpStatus status) {
        return new ApiResponse<>(true, message, data, status.value());
    }

    public static <T> ApiResponse<T> error(String message, HttpStatus status) {
        return new ApiResponse<>(false, message, null, status.value());
    }
}
