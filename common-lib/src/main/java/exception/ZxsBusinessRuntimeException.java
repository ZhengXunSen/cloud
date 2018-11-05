package exception;

import java.text.MessageFormat;

public class ZxsBusinessRuntimeException extends RuntimeException {
    public ZxsBusinessRuntimeException() {
    }

    public ZxsBusinessRuntimeException(String message) {
        super(message);
    }

    public ZxsBusinessRuntimeException(String messagePattern, Object... values) {
        super(MessageFormat.format(messagePattern, values));
    }

    public ZxsBusinessRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZxsBusinessRuntimeException(Throwable cause) {
        super(cause);
    }

    public ZxsBusinessRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
