package com.example.captchabreaker.bean;

import lombok.Data;

import java.util.Map;

@Data
public class WXMessage {

    private String touser;
    private String template_id;
    private String page;
    private Map<String, Object> data;
    private String miniprogram_state;
    private String lang;

}
