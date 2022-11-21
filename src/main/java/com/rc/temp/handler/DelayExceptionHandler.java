/*package com.rc.temp.handler;

import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice("com.rc.temp.controller")
public class DelayExceptionHandler {

    *//**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     * @param binder
     *//*
    @InitBinder
    public void initWEbBinder(WebDataBinder binder){
        //对日期统一处理
        binder.addCustomFormatter(new DateFormatter("yyy-MM-dd"));
        //添加对呼叫的校验
        //binder.setValidator();
    }

    *//**
     * 把值绑定到model中，或设置获得的model中的属性值进行转换，最终使全局@RequestMappting可以获取到该值
     * @param model
     *//*
    @ModelAttribute
    public void addAttribute(Model model){
        model.addAttribute("attribute","The attribute");
        //model.asMap();
        //model.mergeAttributes(map);
    }

    *//*@ResponseBody
    @ExceptionHandler({MyCustomException.class}) //指定拦截异常的类型
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //自定义浏览器返回状态码
    public Map<String,Object> customExceptionHandle(MyCustomException e){
        Map<String,Object> map = new HashMap<>();
        map.put("code",e.getCode());
        map.put("msg",e.getMsg());
        return map;
    }*//*
}*/
