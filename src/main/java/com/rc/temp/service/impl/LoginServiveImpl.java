package com.rc.temp.service.impl;

import com.rc.temp.db.User;
import com.rc.temp.db.entity.LoginUser;
import com.rc.temp.db.entity.ResponseResult;
import com.rc.temp.service.LoginService;
import com.rc.temp.setting.RedisCache;
import com.rc.temp.setting.ResultState;
import com.rc.temp.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Log4j2
@Service
public class LoginServiveImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(@RequestBody User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            log.error("用户名或密码错误");
            throw new RuntimeException("用户名或密码错误");
        }

        //登录成功生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        redisCache.setCacheObject("delay:login:" + userId, loginUser);

        Map<String, String> map = new HashMap<>();
        map.put("delayToken", jwt);
        map.put("userId", userId);
        map.put("name", loginUser.getUsername());
        map.put("permissions", String.valueOf(loginUser.getPermissions()));
        log.info("用户：" + loginUser.getUser().getUsername() + "已登录");
        return new ResponseResult(0, "登录成功", map);
    }

    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Integer userid = loginUser.getUser().getId();
        redisCache.deleteObject("delay:login:" + userid);
        log.info("用户：" + loginUser.getUser().getUsername() + "已退出");
        return new ResponseResult(0, "退出成功");
    }

    @Override
    public ResponseResult tokenErr(HttpServletResponse response, HttpServletRequest request) {
        SecurityContext context = SecurityContextHolder.getContext();
        response.setStatus(401);
        if (context != null && context.getAuthentication() != null) {
            Authentication authentication = context.getAuthentication();
            Object object = authentication.getPrincipal();
            if(Objects.isNull(object)){
                return new ResponseResult(ResultState.FAIL, "authentication为null");
            }
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            //用户未登录或token过期,清除数据
            log.warn("token过期");
            Integer userid = loginUser.getUser().getId();
            redisCache.deleteObject("delay:login:" + userid);
            return new ResponseResult(ResultState.WARNING, "token已过期，请重新登录");
        } else {
            String error = (String) request.getAttribute("loginError");
            log.error(error);
            return new ResponseResult(ResultState.FAIL, error);
        }
    }
}
