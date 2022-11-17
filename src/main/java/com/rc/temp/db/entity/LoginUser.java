package com.rc.temp.db.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.rc.temp.db.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
public class LoginUser implements UserDetails, Serializable {

    private User user;

    private Integer permissions;

    //安全考虑该类型变量不允许直接存入，不序列化
    @JSONField(serialize = false)
    private List<GrantedAuthority> authorities = new ArrayList<>();

    public LoginUser(User user,Integer permissions){
        this.user = user;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //调用获取权限时已经不为空，说明已经赋过值了
        if(authorities.size() != 0){
            return authorities;
        }

        //2楼3楼诊区生成权限
        if(permissions != null && (permissions != 103 || permissions != 104)){
            authorities.add(new SimpleGrantedAuthority("103"));
            authorities.add(new SimpleGrantedAuthority("104"));
        }else {
            authorities.add(new SimpleGrantedAuthority(String.valueOf(permissions)));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
