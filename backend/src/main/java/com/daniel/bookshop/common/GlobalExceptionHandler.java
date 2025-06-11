package com.daniel.bookshop.common;

import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Response<Void> handleException(Exception e) {
        e.printStackTrace();
        return Response.error(Constants.RESULT_CODE_SERVER_ERROR, "服务器内部错误：" + e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Response<Void> handleValidException(Exception e) {
        BindingResult bindingResult;
        if (e instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        } else {
            bindingResult = ((BindException) e).getBindingResult();
        }
        FieldError fieldError = bindingResult.getFieldError();
        if (fieldError != null) {
            return Response.error(Constants.RESULT_CODE_SERVER_ERROR, fieldError.getDefaultMessage());
        }
        return Response.error(Constants.RESULT_CODE_SERVER_ERROR, "参数验证失败");
    }

    @ExceptionHandler(DataAccessException.class)
    public Response<Void> handleDataAccessException(DataAccessException e) {
        e.printStackTrace();
        return Response.error(Constants.RESULT_CODE_SERVER_ERROR, "数据库操作异常：" + e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return Response.error(Constants.RESULT_CODE_SERVER_ERROR, "请求参数格式错误：" + e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Response<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return Response.error(Constants.RESULT_CODE_SERVER_ERROR, "缺少必要参数：" + e.getParameterName());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Response<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return Response.error(Constants.RESULT_CODE_SERVER_ERROR, "参数类型错误：" + e.getName());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return Response.error(Constants.RESULT_CODE_SERVER_ERROR, "不支持的请求方法：" + e.getMethod());
    }
} 