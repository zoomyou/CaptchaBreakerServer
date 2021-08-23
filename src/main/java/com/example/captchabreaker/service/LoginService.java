package com.example.captchabreaker.service;

import com.alibaba.fastjson.JSONObject;
import com.example.captchabreaker.bean.User;
import com.example.captchabreaker.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class LoginService {

    @Autowired
    UserMapper userMapper;

    // 网页登录服务
    public JSONObject webLogin(User loginUser){
        String message = "登录失败！";
        int status = 500;
        User resultUser = new User();
        resultUser.setUser_id(-1);

        // 先查询是否存在
        User judgeUser = userMapper.selectUserByName(loginUser.getUser_name());
        log.info("查询后的用户为： {} ", judgeUser.getUser_name());
        log.info(""+judgeUser);
        if (judgeUser == null){
            message += "用户不存在！";
        } else {
            // 查询是密码是否正确
            if (!judgeUser.getPassword().equals(loginUser.getPassword())){
                message += "密码错误！";
            } else {
                // 如果密码正确则证明登录成功修改返回状态
                status = 200;
                message = "登录成功！";
                resultUser = judgeUser;
            }
        }

        JSONObject res = new JSONObject();
        res.put("status", status);
        res.put("message", message);
        res.put("data", resultUser);

        return res;
    }

}
