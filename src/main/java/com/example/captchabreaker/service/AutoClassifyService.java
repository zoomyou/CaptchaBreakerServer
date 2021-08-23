package com.example.captchabreaker.service;

import com.alibaba.fastjson.JSONObject;
import com.example.captchabreaker.config.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AutoClassifyService {

    @Autowired
    HttpService httpService;
    @Autowired
    GlobalConfig globalConfig;
    @Autowired
    ImgToBase64 imgToBase64;

    public JSONObject autoRecognition(String code, String path) throws Exception{
        if (code.equals("0")){
            return characterRecognition(path);
        }
        if (code.equals("1")){
            return recaptcha3Recognition(path);
        }
        if (code.equals("2")){
            return recaptcha4Recognition(path);
        }
        if (code.equals("3")){
            return moveRecognition(path);
        }else {
            JSONObject res = new JSONObject();
            res.put("status", "500");
            return res;
        }
    }

    public JSONObject characterRecognition(String path) throws Exception{
        JSONObject object = new JSONObject();
        object.put("path", path);
        JSONObject temp = httpService.getRes(globalConfig.getCharactorRecognition(), object);
        JSONObject resObject = new JSONObject();
        resObject.put("status", temp.get("status"));
        resObject.put("bypass_result", temp.get("value"));
        return resObject;
    }

    public JSONObject recaptcha3Recognition(String path) throws Exception{
        String incode = null;
        try {
            incode = imgToBase64.getBase64(path);
        } catch (IOException e) {
            JSONObject res = new JSONObject();
            res.put("status", "500");
            return res;
        }
        JSONObject object = new JSONObject();
        object.put("base_64", incode);
        return  httpService.getRes(globalConfig.getReCaptcha3Recognition(), object);
    }

    public JSONObject recaptcha4Recognition(String path) throws Exception{
        String incode = null;
        try {
            incode = imgToBase64.getBase64(path);
        } catch (IOException e) {
            JSONObject res = new JSONObject();
            res.put("status", "500");
            return res;
        }
        JSONObject object = new JSONObject();
        object.put("data", incode);
        return  httpService.getRes(globalConfig.getReCaptcha4Recognition(), object);
    }

    public JSONObject moveRecognition(String path) throws Exception{
        String incode = null;
        try {
            incode = imgToBase64.getBase64(path);
        } catch (IOException e) {
            JSONObject res = new JSONObject();
            res.put("status", "500");
            return res;
        }
        JSONObject object = new JSONObject();
        object.put("path", path);
        object.put("base_64", incode);
        return httpService.getRes(globalConfig.getMoveRecognition(), object);
    }
}
