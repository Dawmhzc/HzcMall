package com.hzc.demo.commom;


/**
 * 错误类说明
 * @author hzc
 * @date 2021/8/2
 */
public enum ErrorEnum {
    INVALID_PARAMS(1,"参数不合法"),
    DATE_NOEXIT(2,"数据不存在"),
    RIGHT_NOTEXIT(3,"错误权限码"),
    RIGHT_NOTENJOIN(4,"权限不足"),
    USER_NAMEEXIT(5,"用户名已存在"),
    USER_MOBILEEXIT(6,"手机已绑定账号");



    private final int code;
    private final String message;

    ErrorEnum(int code, String message){
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
