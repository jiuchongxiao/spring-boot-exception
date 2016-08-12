package com.wk.plw.error;

/**
 * Created by zhuyanqing on 2016/7/29.自定义异常，参数为自定义信息
 */
public final class AppException extends Exception {
    //错误码
    int code = -1;
    //错误描述
    String msg;

    public AppException(String msg){
        super(msg);
        this.msg=msg;
    }

    public AppException(ExceptionType exceptionType){
        super(exceptionType.getMsg());
        this.code = exceptionType.getCode();
        this.msg = exceptionType.getMsg();
    }

    public AppException(Exception e){
        super(e.getMessage());
    }

    public AppException(int code,String msg){
        super(msg);
        this.msg=msg;
        this.code=code;
    }

}
