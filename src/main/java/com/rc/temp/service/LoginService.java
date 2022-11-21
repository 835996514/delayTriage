package com.rc.temp.service;

import com.rc.temp.db.User;
import com.rc.temp.db.entity.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginService {

    ResponseResult login(@RequestBody User user);

    ResponseResult logout();

    ResponseResult tokenErr(HttpServletResponse response, HttpServletRequest request);

}
