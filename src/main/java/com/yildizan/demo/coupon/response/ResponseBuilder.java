package com.yildizan.demo.coupon.response;

import org.springframework.http.HttpStatus;

public final class ResponseBuilder {

    private ResponseBuilder() {}

    public static ApiResponse success(String message) {
        return SuccessResponse.builder()
                .code(HttpStatus.OK.value())
                .message(message)
                .build();
    }

    public static <T> ApiResponse success(T result) {
        return SuccessResponse.builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }

    public static <T> ApiResponse success(String message, T result) {
        return SuccessResponse.builder()
                .code(HttpStatus.OK.value())
                .message(message)
                .result(result)
                .build();
    }

    public static ApiResponse error(int code, String message) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .build();
    }

}
