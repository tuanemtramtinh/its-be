package com.tuanemtramtinh.itscommon.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    // success with data
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<T>(true, "OK", data);
    }

    // success without data
    public static <T> ApiResponse<T> ok(String message) {
        return new ApiResponse<T>(true, message, null);
    }

    // success with message + data
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<T>(true, message, data);
    }

    // error with message (no data)
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<T>(false, message, null);
    }
}
