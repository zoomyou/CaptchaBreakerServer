package com.example.captchabreaker.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.captchabreaker.bean.Miniuser;
import com.example.captchabreaker.bean.User;
import com.example.captchabreaker.config.MiniProConfig;
import com.example.captchabreaker.mapper.MiniuserMapper;
import com.example.captchabreaker.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    LoginService loginService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    MiniProConfig miniProConfig;
    @Autowired
    MiniuserMapper miniuserMapper;

    @PostMapping("/login")
    public JSONObject webLogin(@RequestBody JSONObject params){

        User loginUser = new User();
        loginUser.setUser_name(params.getString("username"));
        loginUser.setPassword(params.getString("password"));

        log.info("用户 {} 开始登录...", loginUser.getUser_name());

        return loginService.webLogin(loginUser);
    }

    @PostMapping("/xcxlogin")
    public JSONObject xcxLogin(@RequestBody JSONObject code){
        log.info("接收到的数据code为：");
        log.info(""+code);
        JSONObject res = new JSONObject();
        String status = "200";
        String message = "success";

        ResponseEntity<String> responseEntity = restTemplate.exchange(miniProConfig.getMiniProgramAuthCode2Session()+
                        "?appid="+miniProConfig.getMiniProgramAppid()+"&secret="+miniProConfig.getMiniProgramSecret()+
                        "&js_code="+code.get("code")+"&grant_type="+miniProConfig.getMiniProgramGrantType(),
                HttpMethod.GET, null, String.class);
        log.info("返回的信息为："+responseEntity.getBody());
        JSONObject response = JSONObject.parseObject(responseEntity.getBody());
        log.info(response.getString("openid"));

        QueryWrapper<Miniuser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", response.getString("openid"));

        Miniuser miniuser = miniuserMapper.selectOne(queryWrapper);
        if (miniuser == null){
            miniuserMapper.insert(new Miniuser(null, response.getString("openid"), null));
        }

        res.put("status", status);
        res.put("message", message);
        res.put("openid", response.getString("openid"));

        return res;
    }
}
