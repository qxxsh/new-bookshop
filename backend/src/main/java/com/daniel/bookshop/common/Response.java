package com.daniel.bookshop.common;

import lombok.Data;

@Data
public class Response<T> {
    private int code;
    private String message;
    private T data;

    public Response() {
        this.code = Constants.RESULT_CODE_SUCCESS;
        this.message = Constants.RESULT_MESSAGE_SUCCESS;
    }

    public Response(T data) {
        this();
        this.data = data;
    }

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Response<T> success() {
        return new Response<>();
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(data);
    }

    public static <T> Response<T> error(String message) {
        return new Response<>(Constants.RESULT_CODE_SERVER_ERROR, message);
    }

    public static <T> Response<T> error(int code, String message) {
        return new Response<>(code, message);
    }
} 