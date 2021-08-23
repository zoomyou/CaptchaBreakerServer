package com.example.captchabreaker.service;

import com.alibaba.fastjson.JSONObject;
import com.example.captchabreaker.config.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutoRecognizeService {

    @Autowired
    HttpService httpService;
    @Autowired
    GlobalConfig globalConfig;

    public String autoRecognize(String path, String data, String cate_code){
        // 构造请求体对象
        JSONObject param = new JSONObject();
        param.put("path", path);
        param.put("base_64", data);

        // 请求后台
        JSONObject Res = httpService.getRes(globalConfig.getAutoRecognitionUrl()+cate_code, param);

        return Res.getString("bypass_result");
    }

}
