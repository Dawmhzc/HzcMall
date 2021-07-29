package com.hzc.demo.commom;


/**
 * 错误类，定义错误码跟犯错信息
 */
public enum Error {
    INVALID_PARAMS(1,"参数不合法");


    private final int code;
    private final String message;

    Error(int code,String message){
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
