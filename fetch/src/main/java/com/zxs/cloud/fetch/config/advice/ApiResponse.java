package com.zxs.cloud.fetch.config.advice;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * API调用的通用结果
 * Created by roman.luo on 2017/7/18.
 */
@Data
public class ApiResponse<T> {

    private int code;

    private String message;

    private T data;

    private ApiResponse() {
        this(null);
    }

    private ApiResponse(T data) {
        this(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ApiResponse success(Object data) {
        return new ApiResponse(data);
    }

    public static ApiResponse failure(String message) {
        return failure(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    public static ApiResponse failure(int statusCode, String message) {
        return new ApiResponse(statusCode, message, null);
    }
}
