package com.example.captchabreaker.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ThreeService {
    @Autowired
    HttpService httpService;

    public String getRes(String typeid, String image, String id){
        String username= "vagpoem";
        String password= "wx16060708";

        JSONObject data = new JSONObject();

        data.put("username", username);
        data.put("password", password);

        if (typeid != null && !typeid.equals("")){
            data.put("typeid", typeid);
        }

        data.put("image", image);


        String url = "http://api.ttshitu.com/predict";

        JSONObject resJson = new JSONObject();

        try {
            resJson = httpService.getRes(url, data);
        } catch (Exception e){
            return null;
        }

        log.info("jobid = "+id+" result = "+resJson.getJSONObject("data").getString("result"));

        return resJson.getJSONObject("data").getString("result");
    }
}
