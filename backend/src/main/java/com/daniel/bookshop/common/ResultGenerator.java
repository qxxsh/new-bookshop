package com.daniel.bookshop.common;

import com.daniel.bookshop.common.Result;
import com.daniel.bookshop.common.Constants;

public class ResultGenerator {
    public static <T> Result<T> genSuccessResult() {
        return new Result<T>()
                .setCode(Constants.RESULT_CODE_SUCCESS)
                .setMessage(Constants.RESULT_MESSAGE_SUCCESS);
    }

    public static <T> Result<T> genSuccessResult(T data) {
        return new Result<T>()
                .setCode(Constants.RESULT_CODE_SUCCESS)
                .setMessage(Constants.RESULT_MESSAGE_SUCCESS)
                .setData(data);
    }

    public static <T> Result<T> genFailResult(String message) {
        return new Result<T>()
                .setCode(Constants.RESULT_CODE_SERVER_ERROR)
                .setMessage(message);
    }

    public static <T> Result<T> genUnauthorizedResult() {
        return new Result<T>()
                .setCode(Constants.RESULT_CODE_UNAUTHORIZED)
                .setMessage(Constants.RESULT_MESSAGE_UNAUTHORIZED);
    }

    public static <T> Result<T> genForbiddenResult() {
        return new Result<T>()
                .setCode(Constants.RESULT_CODE_FORBIDDEN)
                .setMessage(Constants.RESULT_MESSAGE_FORBIDDEN);
    }
} 