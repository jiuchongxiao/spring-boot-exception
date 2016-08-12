package com.wk.plw.error;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhuyanqing on 2016/8/11.  测试的例子
 */
@RequestMapping("/test")
@Controller
public class TestController {

    @RequestMapping("/test1")
    public String test1(HttpServletRequest request) throws Exception{
        try {
            int i=0;
            int j=1;
            System.out.println(j/i);
        }catch (Exception e){
            throw e;
        }

        return "hello";
    }


    @RequestMapping("/test2")
    public void test2() throws Exception{
        try {
            int i=0;
            int j=1;
            System.out.println(j/i);
        }catch (Exception e){
            //throw new AppException(e.getMessage());
            throw new AppException(ExceptionType.unsupported_media_type);
        }
    }

    @RequestMapping("/test3")
    public void test3() throws Exception{
        try {
            int i=0;
            int j=1;
            System.out.println(j/i);
        }catch (Exception e){
            throw new AppException(-1,"分母不能为空");
        }
    }

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/
    //直接抛出异常
    @ResponseBody
    @RequestMapping("/test4")
    public JSONObject test4() throws Exception{
        JSONObject jsonObject = new JSONObject();
        try {
            int i=0;
            int j=1;
            System.out.println(j/i);
            jsonObject = ResponseUtils.responseSuccess(jsonObject, "成功");
        }catch (Exception e){
            throw e;
        }
        return jsonObject;
    }
    //抛出自定义异常
    @ResponseBody
    @RequestMapping("/test5")
    public JSONObject test5() throws Exception{
        JSONObject jsonObject = new JSONObject();
        try {
            /*int i=0;
            int j=1;
            System.out.println(j/i);
            jsonObject = responseSuccess(jsonObject,"成功");*/
            File file = new File("D:\\projects\\spring-boot-master\\a.txt");
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read();

        }catch (Exception e){
            //throw new AppException(-12,e.getMessage());
            throw e;
        }
        return jsonObject;

    }



    //直接返回成功
    @ResponseBody
    @RequestMapping("/test6")
    public JSONObject test6() throws Exception{
        JSONObject jsonObject = new JSONObject();
        List<String> list = Arrays.asList("aaa", "bbb", "ccc");
        jsonObject.put("list",list);
        return ResponseUtils.responseSuccess(jsonObject, "成功");
    }

    //直接返回失败
    @ResponseBody
    @RequestMapping("/test7")
    public JSONObject test7() throws Exception{
        JSONObject jsonObject = new JSONObject();
        return ResponseUtils.responseFail(jsonObject, "失败", -2);
    }

    //抓异常，返回成功或失败,不走错误拦截ErrorControl
    @ResponseBody
    @RequestMapping("/test8")
    public JSONObject test8() throws Exception{
        JSONObject jsonObject = new JSONObject();
        try {
            int i=0;
            int j=1;
            System.out.println(j/i);
            jsonObject = ResponseUtils.responseSuccess(jsonObject, "成功");
        }catch (Exception e){
            jsonObject = ResponseUtils.responseFail(jsonObject, "失败", -3);
        }
        return jsonObject;

    }

    //不抓异常,走错误拦截ErrorControl，同方法7
    @ResponseBody
    @RequestMapping("/test9")
    public JSONObject test9() throws Exception{
        JSONObject jsonObject = new JSONObject();
        int i=0;
        int j=1;
        System.out.println(j/i);
        return  ResponseUtils.responseSuccess(jsonObject, "成功");

    }


}
