package com.wk.plw.error;

/**
 * Created by zhuyanqing on 2016/8/5.
 *
 *    20502
 *    2 	                    05 	        02
 *    服务级错误（1为系统级错误） 	服务模块代码 	具体错误代码
 *
 *     错误级别（1,2）
 *   1：系统级错误：指导致系统异常或错误的操作导致的错误 如：访问超时，500错误。。。
 *   2：服务级错误：指用户操作不合法的一些错误，如：用户名不存在，用户上传文件太大。。。
 *
 *     服务模块：（模块划分，待扩充）（1--99），默认为00，不指定模块，多模块使用
 *   1：
 *   2：
 *   具体错误代码（1--99），若果超过100个，可以扩展到101,102...
 *   1：
 *   2：
 */


public enum ExceptionType {

    syserror(10001, "系统内部错误"),  //系统错误，包括各种IO,Runtime exception等,如需查看具体完整信息，请访问拦截异常方法
    badrequest(10002, "服务器无法理解的请求，可能是参数类型不匹配"),  //比如 TypeMismatchException
    notfound(10003, "服务器无法找到所请求的页面"),  //
    requestimeout(10004, "请求需要的时间比服务器能够等待的时间长，超时"),  //
    IOexception(10005, "文件上传下载失败"),  //
























    /*--------------------------------------------------------------------------------------------------------------------------------*/
    unauthorized(20001, "需要登录的访问"),  //用户不存在，适用于各种场景的用户不存在
    nothisuser(20002, "用户不存在"),  //用户不存在，适用于各种场景的用户不存在
    forbidden(20003, "无访问权限"),  //无权限使用各种场景
    request_entity_too_large(20004, "服务器不接受该请求，因为请求实体过大"),
    request_url_too_long(20005, "服务器不接受该请求，因为 URL 太长。当您转换一个 \"post\" 请求为一个带有长的查询信息的 \"get\" 请求时发生"),
    unsupported_media_type(20006, "服务器不接受该请求，因为媒体类型不被支持"),
    file_size_too_large(20007, "文件太大"),
    does_multipart_upload(20008, "请确保使用multipart上传文件");



    private final int code;
    private final String msg;

    private ExceptionType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }
    public String getMsg(){ return msg;}

    public static ExceptionType valueOfCode(int code) {
        for (ExceptionType exceptionType : ExceptionType.values()) {
            if (exceptionType.getCode()==code) {
                return exceptionType;
            }
        }
        return null;
    }




}
