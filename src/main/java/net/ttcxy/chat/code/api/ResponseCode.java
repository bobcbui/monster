package net.ttcxy.chat.code.api;

/**
 * 枚举了一些常用API操作码
 * Created by macro on 2019/4/19.
 */
public enum ResponseCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限");
    private final int code;
    private final String message;

    private ResponseCode(int code, String message) {

        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
