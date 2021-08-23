package com.example.captchabreaker.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpService {

    @Autowired
    RestTemplate restTemplate;

    public JSONObject getRes(String url, JSONObject params){
        JSONObject res = new JSONObject();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> entity = new HttpEntity(params, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String responseBody = responseEntity.getBody();
            res = JSONObject.parseObject(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            res = null;
        }

        return res;
    }

}
