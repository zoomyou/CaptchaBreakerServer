package com.example.captchabreaker.service;


import com.alibaba.fastjson.JSONObject;
import com.example.captchabreaker.config.GlobalConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ImgSaveService {

    @Autowired
    GlobalConfig globalConfig;
    @Autowired
    HttpService httpService;

    // 用于保存数据的方法
    public boolean save(JSONObject params, String path){
        String src_type = params.getString("src_type");

        if (src_type.equals("1")){
            return base64Save(params.getString("data"), path);
        }
        if (src_type.equals("2")){
            return urlSave(params.getString("data"), path);
        }

        return false;
    }

    // 用于保存base64数据的方法
    public boolean base64Save(String base64, String path){

        log.info("开始保存base64编码图片");

        boolean flag = false;

        // 1.先将base64编码转换为合适的格式
        String tempbase64 = base64;
        int end = base64.indexOf(",");
        if (end>0){
            tempbase64 = base64.substring(end + 1);
        }

        log.info(tempbase64.substring(0,30));

        // 2.构造base64数据保存对象
        JSONObject base64object = new JSONObject();
        base64object.put("str", tempbase64);
        base64object.put("path", path);

        // 3.调用base64数据保存接口
        JSONObject isSave = httpService.getRes(globalConfig.getSaveBase64DataUrl(), base64object);
        JSONObject isSave2 = httpService.getRes("http://127.0.0.1:5000/savebase64", base64object);

        // 4.判断存储状态
        if (!ObjectUtils.isEmpty(isSave)){
            if (isSave.getString("status").equals("200")){
                if (isSave2.getString("status").equals("200")){
                    log.info("base64图片保存成功");
                    flag = true;
                }
            }
        }

        return flag;
    }

    // 用于保存url数据的方法
    public boolean urlSave(String url, String path){
        boolean flag = false;

        // 1.构造url数据保存对象
        Map<String, String> urlJson = new HashMap<>();
        urlJson.put("url", url);
        urlJson.put("path", path);

        // 2.调用保存接口
        JSONObject object = (JSONObject) JSONObject.toJSON(urlJson);
        JSONObject isSave = httpService.getRes(globalConfig.getSaveUrlDataUrl(), object);
        JSONObject isSave2 = httpService.getRes("http://127.0.0.1:5000/saveurl", object);

        // 3.判断存储状态
        if (!ObjectUtils.isEmpty(isSave)){
            if (isSave.getString("status").equals("200")){
                if (isSave2.getString("status").equals("200")){
                    flag = true;
                }
            }
        }

        return flag;
    }

    // 用于保存链接数据的方法
    private boolean linkSave(String link, String path){
        boolean flag = false;

        // 1.构造link数据保存对象
        // LinkJson linkJson = new LinkJson(link, path);

        return flag;
    }

    public static void main(String[] args) {
        String testg = "fdasfasdf,fsdafads";
        int index = testg.indexOf(",");
        System.out.println(testg.substring(0,10));

        System.out.println(new Timestamp(System.currentTimeMillis()));
    }
}