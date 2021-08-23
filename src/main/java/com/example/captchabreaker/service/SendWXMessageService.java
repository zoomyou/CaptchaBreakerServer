package com.example.captchabreaker.service;

import com.alibaba.fastjson.JSONObject;
import com.example.captchabreaker.bean.TemplateData;
import com.example.captchabreaker.bean.WXMessage;
import com.example.captchabreaker.config.MiniProConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SendWXMessageService {

    @Autowired
    HttpsService httpsService;
    @Autowired
    MiniProConfig miniProConfig;

    /**
     * 获取访问凭证
     * @return token
     */
    public String getAccessToken(){
        String res = "-1", key = "accessToken";

        // 获取接口调用凭证的url
        String url = miniProConfig.getMiniProgramAuthGetAccessToken() +
                "?grant_type=client_credential&appid=" + miniProConfig.getMiniProgramAppid() +
                "&secret=" + miniProConfig.getMiniProgramSecret();
        JSONObject token = httpsService.getResponse(url, HttpMethod.GET);

        return token.getString("access_token");
    }

    public JSONObject push(WXMessage wxMessage){

        String url = miniProConfig.getMiniProgramSubscribeMessageSend() +
                "?access_token=" + getAccessToken();

        return httpsService.postResponse(url, wxMessage);
    }

    public JSONObject captchaPush(String openid, String code){
        String title = "打码任务请求";
        if (code.equals("3")){
            title += ",请在浏览器中打码";
        } else {
            title += ",小程序即可完成打码";
        }
        WXMessage wxMessage = new WXMessage();
        wxMessage.setTouser(openid);
        wxMessage.setTemplate_id("-pOzfH2vipyPAS7hklp925Ps0YckYE_nsHzVQbBWgLQ");
        wxMessage.setPage("pages/main/main");
        wxMessage.setMiniprogram_state("formal");
        TemplateData data1 = new TemplateData(title);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TemplateData data2 = new TemplateData(simpleDateFormat.format(new Date()));
        TemplateData data3 = new TemplateData("打码请求单位");
        TemplateData data4 = new TemplateData("打码任务");
        Map<String, Object> map = new HashMap<>();
        map.put("thing1", data1);
        map.put("thing2", data2);
        map.put("name3", data3);
        map.put("thing4", data4);
        wxMessage.setData(map);
        wxMessage.setLang("zh_CN");
        return push(wxMessage);
    }
}
