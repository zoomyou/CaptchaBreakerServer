package com.example.captchabreaker.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpsService {

    @Autowired
    RestTemplate restTemplate;

    /**
     * 发送get请求
     * @param url
     * @param method
     * @return
     */
    public JSONObject getResponse(String url, HttpMethod method){

        // 发送请求获取返回体参数
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, null, String.class);
        // 将相应的字符串转化为JSON对象便于访问
        JSONObject response = JSONObject.parseObject(responseEntity.getBody());

        return response;
    }

    /**
     * 发送post请求
     * @param url
     * @param object
     * @return
     */
    public JSONObject postResponse(String url, Object object){
        // 发送post请求获取返回参数
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, object, String.class);

        return JSONObject.parseObject(responseEntity.getBody());
    }

    public JSONObject postResponse(String url, Object object, String headPara){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("content-type", headPara);
        HttpEntity<String> httpEntity = new HttpEntity<String>(object.toString(), httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        return JSONObject.parseObject(responseEntity.getBody());
    }

}
