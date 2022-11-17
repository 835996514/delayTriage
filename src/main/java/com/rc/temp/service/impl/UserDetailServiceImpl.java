package com.rc.temp.service.impl;

import com.rc.temp.db.User;
import com.rc.temp.db.entity.LoginUser;
import com.rc.temp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByUsernameEquals(username);
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }

          Integer areaId = user.getAreaId();

        return new LoginUser(user,areaId);
    }
}
