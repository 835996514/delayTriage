/*package com.rc.temp.handler;

import com.rc.temp.setting.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MyAspect {

    *//*@Autowired
    private RedisTemplate redisTemplate;

    @AfterThrowing(value = "execution(* com.rc.temp.setting.RedisCache.*(..))",throwing = "ex")
    public void myAfterThrowing(Throwable ex){
        log.info("redis操作抛出了异常ex：",ex);
        boolean flag = ex instanceof RedisConnectionFailureException;
        if(flag){
            LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
            connectionFactory.setHostName("192.168.190.140");
            connectionFactory.setPort(6379);
            connectionFactory.setValidateConnection(true);
            connectionFactory.afterPropertiesSet();
            connectionFactory.initConnection();
            redisTemplate.setConnectionFactory(connectionFactory);
        }
    }*//*
}*/
