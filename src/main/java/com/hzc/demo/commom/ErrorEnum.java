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
    USER_MOBILEEXIT(6,"手机已绑定账号"),
    ORDER_ERRER(7,"错误的订单信息"),
    ERROR_STATUS(8,"错误的订单状态"),
    INPUT_FAIL(9,"请输入账号或者密码"),
    LOGIN_STATE(10,"请登录账号"),
    ADDRESS_INPUT(11,"请输入收货地址信息"),
    LOGIN_FAIL(12,"账号或密码错误");



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
