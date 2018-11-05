package exception;

public class ZxsParamsException extends ZxsSystemRuntimeException {

    private static final int EXCEPTION_CODE = 400;

    public ZxsParamsException(String message) {
        super(EXCEPTION_CODE, message);
    }

}
