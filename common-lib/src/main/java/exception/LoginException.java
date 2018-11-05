package exception;

/**
 * Created by ping.chen on 2018/1/3.
 */
public class LoginException extends Exception {

    public static final int NAME_PASSWORD_ERR = 401;

    public static final int UNLOGIN_ERR = 403;

    private final int errCode;

    public LoginException(int errCode,String message){
        super(message);
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }
}
