package com.rc.temp.filter;

import com.rc.temp.db.entity.LoginUser;
import com.rc.temp.setting.RedisCache;
import com.rc.temp.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("delayToken");
        if(!StringUtils.hasText(token)){
            //没有token不进行token校验，直接放行进行登录验证（放行接口请求头不带token，不然走下面验证又被拦截）
            filterChain.doFilter(request,response);
            //过滤器执行链响应时执行doFilter后面逻辑，所以需要在此return掉
            return;
        }

        //解析token
        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }

        //根据token解析出来的userId去redis中获取用户信息
        String redisKey = "delay:login:"+userId;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);

        //token能解析成功，说明用户曾经登录过（合法token），若此时redis中无此用户信息，说明已经过期或退出了导致token失效，需要用户再次登录
        if(Objects.isNull(loginUser)){
            throw new RuntimeException("用户未登录");
        }

        //存入SecurityContextHolder（使得authentication对象供后续执行链调用）
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request,response);
    }


}
