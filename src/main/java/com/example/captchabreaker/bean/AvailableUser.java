package com.example.captchabreaker.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class AvailableUser {

    // 向容器中添加一个网页端在线用户集合
    @Bean(name = "webOnlineUserList")
    public Set<String> webOnlineUserList(){
        Set<String> webList = new HashSet<>();
        Collections.synchronizedSet(webList);
        return webList;
    }

    // 向容器中添加一个小程序端在线用户集合
    @Bean(name = "miniProOnlineUserList")
    public Set<String> miniProOnlineUserList(){
        Set<String> miniList = new HashSet<>();
        Collections.synchronizedSet(miniList);
        return miniList;
    }

}
