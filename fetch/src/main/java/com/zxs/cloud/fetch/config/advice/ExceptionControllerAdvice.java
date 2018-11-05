package com.zxs.cloud.fetch.config.advice;

import exception.ZxsEnumRuntimeException;
import exception.ZxsParamsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一处理controller类抛出的FinDcRuntimeException异常，转换成标准json输出格式信息
 * Created by jackie.yu on 2017/7/27.
 * 参考 springboot Error Handling
 */
@ControllerAdvice(basePackages = "com.zxs.cloud.fetch.controller")
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    ResponseEntity handleControllerException(HttpServletRequest request, Throwable ex) {
        HttpStatus status = getStatus(request);
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();  // 默认500，服务器内部异常
        if (ex instanceof ZxsParamsException) {
            logger.info("接口访问异常[" + request.getRequestURI() + "]", ex);
        } else {
            logger.error("接口访问异常[" + request.getRequestURI() + "]", ex);
        }
        if (ex instanceof ZxsParamsException) {
            statusCode = ((ZxsParamsException) ex).getErrCode();
        }
        if (ex instanceof ZxsEnumRuntimeException) {
            statusCode = ((ZxsEnumRuntimeException) ex).getErrorCode();
        }
        return new ResponseEntity(ApiResponse.failure(statusCode, ex.getMessage()), status);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.OK;
        }
        return HttpStatus.valueOf(statusCode);
    }

}
