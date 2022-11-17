package com.rc.temp.service;

import com.rc.temp.db.User;
import com.rc.temp.db.entity.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;

public interface LoginService {

    ResponseResult login(@RequestBody User user);

    ResponseResult logout();

}
