package exception;

import lombok.Getter;

import java.text.MessageFormat;

@Getter
public class ZxsSystemRuntimeException extends RuntimeException {

    private static final int DEFAULT_ERR_CODE = 500;

    private final int errCode;

    public ZxsSystemRuntimeException() {
        this(DEFAULT_ERR_CODE);
    }

    public ZxsSystemRuntimeException(int errCode) {
        this(errCode, (String) null);
    }

    public ZxsSystemRuntimeException(String message) {
        this(DEFAULT_ERR_CODE, message);
    }

    public ZxsSystemRuntimeException(String message, Throwable cause) {
        this(DEFAULT_ERR_CODE, message, cause);
    }

    public ZxsSystemRuntimeException(String messagePattern, Object... values) {
        this(DEFAULT_ERR_CODE, MessageFormat.format(messagePattern, values));
    }

    public ZxsSystemRuntimeException(Throwable cause) {
        this(DEFAULT_ERR_CODE, cause);
    }

    public ZxsSystemRuntimeException(int errCode, String message) {
        this(errCode, message, null);
    }

    public ZxsSystemRuntimeException(int errCode, String message, Throwable cause) {
        this(errCode, message, cause, true, true);
    }

    public ZxsSystemRuntimeException(int errCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errCode = errCode;
    }

    public ZxsSystemRuntimeException(int errCode, Throwable cause) {
        this(errCode, (cause == null ? null : cause.toString()), cause);
    }
}
