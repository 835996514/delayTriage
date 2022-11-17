package com.rc.temp.controller;

import com.rc.temp.db.User;
import com.rc.temp.db.entity.ResponseResult;
import com.rc.temp.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
