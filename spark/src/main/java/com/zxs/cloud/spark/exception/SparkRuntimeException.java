package com.zxs.cloud.spark.exception;

/**
 * Created by jackie.yu on 2017/7/27.
 */
public class SparkRuntimeException extends RuntimeException {

    private static final int EXCEPTION_CODE = 500 ;

    public SparkRuntimeException(){
        super();
    }

    public SparkRuntimeException(String message){
        super(message);
    }

    public SparkRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SparkRuntimeException(Throwable cause) {
        super(cause);
    }

    public SparkRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SparkRuntimeException(Exception e){
        super(e);
    }

    public int getExceptionCode(){
        return EXCEPTION_CODE;
    }
}
