package com.wk.plw.error;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhuyanqing on 2016/8/8.
 */
public class ResponseUtils {

    /**
     * 返回成功
     * @param jsonObject
     * @param msg
     * @return
     */
    public static JSONObject responseSuccess(JSONObject jsonObject, String msg) {
        HttpServletResponse resp = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        resp.setHeader("code","200");
        jsonObject.put("code", 200);
        jsonObject.put("msg", msg);
        return jsonObject;
    }

    /**
     * 返回失败，可以自定义code,msg，常用于抓异常后返回
     * @param jsonObject
     * @param msg
     * @param code
     * @return
     */
    public static JSONObject responseFail(JSONObject jsonObject, String msg, int code) {
        HttpServletResponse resp = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        resp.setHeader("code","-1");
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        return jsonObject;
    }

    public static JSONObject responseFail(JSONObject jsonObject, ExceptionType exceptionType) {
        HttpServletResponse resp = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        resp.setHeader("code","-1");
        jsonObject.put("code", exceptionType.getCode());
        jsonObject.put("msg", exceptionType.getMsg());
        return jsonObject;
    }

}
