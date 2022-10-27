package net.ttcxy.chat.code.api;

/**
 * 自定义API异常
 * @author huanglei
 */
public class ApiException extends RuntimeException implements IErrorCode {

    private int errorCode;

    public ApiException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApiException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.errorCode = responseCode.getCode();
    }

    public ApiException() {
        super(ResponseCode.FAILED.getMessage());
        this.errorCode = ResponseCode.FAILED.getCode();
    }

    @Override
    public int getCode() {
        return errorCode;
    }

    public ApiException(String message) {
        super(message);
        this.errorCode = ResponseCode.FAILED.getCode();
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

}
