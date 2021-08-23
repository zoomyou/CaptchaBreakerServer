package com.example.captchabreaker.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LogoutController {

    @GetMapping("/logout")
    public JSONObject logout(@RequestParam Integer user_id){
        JSONObject res = new JSONObject();

        log.info("用户 {} 开始退出 ...",user_id);

        res.put("status", "200");
        res.put("message", "退出成功！");

        return res;
    }

}
