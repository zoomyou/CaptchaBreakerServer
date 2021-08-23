package com.example.captchabreaker.bean;

import lombok.Data;

@Data
public class JobMessage {

    private String job_id;
    private String src_type;
    private String data;
    private String captcha_type;
    private String path;

    public JobMessage(String job_id, String src_type, String data, String captcha_type){
        this.job_id = job_id;
        this.src_type = src_type;
        this.data = data;
        this.captcha_type = captcha_type;
    }

}
