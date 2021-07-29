package com.hzc.demo.commom;

import lombok.Data;

/**
 * 返回结果包装类，方便接口调用
 */
@Data
public class Result {
    private final Object data;
    private final int code;
    private final String message;

    public Result(Object data,int code,String message){
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public Result OK(){
        return new Result(null,0,null);
    }

    public Result OK(Object data){
        return new Result(data,0,null);
    }

    public Result fail(Error error){
        return new Result(null,error.getCode(),error.getMessage());
    }

    public Result fail(Error error,String message){
        return new Result(null,error.getCode(),message);
    }
}
