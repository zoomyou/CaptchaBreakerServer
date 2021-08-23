package com.example.captchabreaker.service;

import com.alibaba.fastjson.JSONObject;
import com.example.captchabreaker.config.GlobalConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
public class ClassifyService {

    @Autowired
    HttpService httpService;
    @Autowired
    GlobalConfig globalConfig;

    public String classify(String path){
        String type = "7";

        // 1.构造分类api接口的数据对象
        JSONObject object = new JSONObject();
        object.put("path", path);

        // 2.调用分类接口
        JSONObject isClass = httpService.getRes(globalConfig.getPhotoClassifyUrl(), object);


        // 3.对分类结果进行解析
        if (!ObjectUtils.isEmpty(isClass)){
            if (isClass.getString("code").equals("200")){
                type = isClass.getString("type");
            }
        }

        log.info("识别的结果为：{}", type);

        return type;
    }
}
