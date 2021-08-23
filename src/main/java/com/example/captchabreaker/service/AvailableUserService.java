package com.example.captchabreaker.service;

import com.example.captchabreaker.config.GlobalConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class AvailableUserService {

    @Autowired
    Set<String> webOnlineUserList;
    @Autowired
    Set<String> miniProOnlineUserList;
    @Autowired
    GlobalConfig globalConfig;

    // 向空闲用户列表中添加用户
    public boolean webAdd(String id){
        log.info("向网页列表中添加用户");
        return webOnlineUserList.add(id);
    }

    // 从空闲用户列表中删除用户
    public boolean webDelete(String id){
        log.info("从网页列表中删除用户");
        return webOnlineUserList.remove(id);
    }

    // 分配一个空闲用户返回一个字符串
    public String webSchedule(){
        String id = null;

        // 如果列表不为空返回用户id
        if (!webOnlineUserList.isEmpty()){
            id = webOnlineUserList.iterator().next();
            webOnlineUserList.remove(id);
        }

        return id;
    }

    // 向空闲用户列表中添加用户
    public boolean miniAdd(String id){
        log.info("向小程序列表中添加了用户！");
        return miniProOnlineUserList.add(id);
    }

    // 从空闲用户列表中删除用户
    public boolean miniDelete(String id){
        log.info("从小程序列表中删除用户！");
        return miniProOnlineUserList.remove(id);
    }

    // 分配一个空闲用户返回一个字符串
    public String miniSchedule(){
        String id = null;
        // 如果列表不为空返回用户id
        if (!miniProOnlineUserList.isEmpty()){
            id = miniProOnlineUserList.iterator().next();
            miniProOnlineUserList.remove(id);
        }

        return id;
    }

    public String appointUser(String code){
        String user = null;
        int getUser = 0;
        while (user == null && getUser<globalConfig.getAvailableUserGetTime()){
            user = schedule(code);
            try {
                Thread.sleep(globalConfig.getTimeSlot());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getUser++;
        }
        return user;
    }

    // 向空闲用户列表中添加用户
    public boolean add(String id, String type){
        if (type.equals("mini")){
            return miniAdd(id);
        } else {
            return webAdd(id);
        }
    }

    // 从空闲用户列表中删除用户
    public boolean delete(String id, String type){
        if (type.equals("mini")){
            return miniDelete(id);
        } else {
            return webDelete(id);
        }
    }

    // 分配一个空闲用户返回一个字符串
    public String schedule(String code){
        String user = null;
        if (code.equals("3")){
            log.info("分配给网页端");
            user = webSchedule();
        } else {
            log.info("优先分配给小程序");
            user = miniSchedule();
            if (user == null){
                user = webSchedule();
            }
        }

        return user;
    }

}
