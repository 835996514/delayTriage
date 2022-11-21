package com.rc.temp.controller;

import com.rc.temp.db.User;
import com.rc.temp.db.entity.ResponseResult;
import com.rc.temp.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/user")
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseResult login(User user){
        return loginService.login(user);
    }

    @GetMapping("/logout")
    public ResponseResult logOut(){
        return loginService.logout();
    }

    @RequestMapping("/tokenErr")
    public ResponseResult clearUser(HttpServletResponse response, HttpServletRequest request){
        return loginService.tokenErr(response,request);
    }
}
