/*
 * 文件名： ErrorControl.java
 * 创建日期： 2016年5月24日
 * Copyright(C) 2016, by linY.
 * 作者: linY [linyang@99114.com]
 *
 */
package com.wk.plw.error;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;


/**
* 错误控制
* @author linY [linyang@99114.com]
* @version
* @since 2016年5月24日
*/
//如果不开启此注解，则有异常时会直接跳转到500页面,而不是重定向
@ControllerAdvice
public class ErrorControl {

   public static Logger logger = Logger.getLogger(ErrorControl.class);//log object

   @SuppressWarnings("deprecation")
   @ExceptionHandler(value = { Exception.class, RuntimeException.class })
   @ResponseBody
   public ResponseEntity<Map<String, Serializable>> defaultErrorHandler(HttpServletRequest request, Exception ex,HttpServletResponse response) throws Exception{
       ex.printStackTrace(System.err);
       Map<String, Serializable> errMap = new HashMap<String, Serializable>();

       //返回访问路径
       String servletPath = request.getRequestURI();
       //String servletPath2 = request.getRequestURI()+"?"+request.getQueryString();;
       errMap.put("request",servletPath);
       //查看完整报错信息
       if (ObjectUtils.equals(request.getParameter("debug"), "all")) {
           StringWriter sw = new StringWriter();
           PrintWriter pw = new PrintWriter(sw);
           pw.flush();
           ex.printStackTrace(pw);
           pw.close();
           errMap.put("__debug__", sw.toString());
       }
       //跳转到500页面
/*       else{
           try {
               response.sendRedirect("/sys/500");
           }catch (Exception e){
                 throw e;
           }
       }*/
       HttpStatus status = getStatus(ex);

       //如果是自定义AppException,则直接返回
       if(ex instanceof AppException){
           errMap.put("code",((AppException) ex).code);
           errMap.put("msg",ex.getMessage());
       }else{
           //错误判断
           AppException appException = null;
           //IO异常
           if (ex instanceof IOException) {
               appException = new AppException(ExceptionType.IOexception);
           }
           //runtime异常
           else{
               if (ObjectUtils.equals(status, HttpStatus.BAD_REQUEST)) {
                   appException = new AppException(ExceptionType.badrequest);
               } else if (ObjectUtils.equals(status, HttpStatus.UNAUTHORIZED)) {
                   appException = new AppException(ExceptionType.unauthorized);
               } else {
                   appException = new AppException(ExceptionType.syserror);
               }
           }

           errMap.put("code",appException.code);
           errMap.put("msg", appException.getMessage());
       }

       ResponseEntity<Map<String, Serializable>> responseEntity = new ResponseEntity<Map<String, Serializable>>(errMap, status);
       response.setHeader("code","-1");
       return responseEntity;
   }

   private HttpStatus getStatus(Exception ex) {
       Integer statusCode = getStatusFromException(ex);
       if (statusCode == null) {
           return HttpStatus.INTERNAL_SERVER_ERROR;
       }
       try {
           return HttpStatus.valueOf(statusCode);
       } catch (Exception e) {

           return HttpStatus.INTERNAL_SERVER_ERROR;
       }
   }

   protected Integer getStatusFromException(Exception ex) {
       // 业务错误
       if (ex instanceof IOException) {
           return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
       }else if (ex instanceof NoSuchRequestHandlingMethodException) {
           return HttpServletResponse.SC_NOT_FOUND;
       } else if (ex instanceof HttpRequestMethodNotSupportedException) {
           return HttpServletResponse.SC_METHOD_NOT_ALLOWED;
       } else if (ex instanceof HttpMediaTypeNotSupportedException) {
           return HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;
       } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
           return HttpServletResponse.SC_NOT_ACCEPTABLE;
       } else if (ex instanceof MissingPathVariableException) {
           return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
       } else if (ex instanceof MissingServletRequestParameterException) {
           return HttpServletResponse.SC_BAD_REQUEST;
       } else if (ex instanceof ServletRequestBindingException) {
           return HttpServletResponse.SC_BAD_REQUEST;
       } else if (ex instanceof ConversionNotSupportedException) {
           return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
       } else if (ex instanceof TypeMismatchException) {
           return HttpServletResponse.SC_BAD_REQUEST;
       } else if (ex instanceof HttpMessageNotReadableException) {
           return HttpServletResponse.SC_BAD_REQUEST;
       } else if (ex instanceof HttpMessageNotWritableException) {
           return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
       } else if (ex instanceof MethodArgumentNotValidException) {
           return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
       } else if (ex instanceof MissingServletRequestPartException) {
           return HttpServletResponse.SC_BAD_REQUEST;
       } else if (ex instanceof BindException) {
           return HttpServletResponse.SC_BAD_REQUEST;
       }
//		} else if (ex instanceof AccessDeniedException) {
//			return HttpServletResponse.SC_UNAUTHORIZED;
//		}
       return null;
   }



}
