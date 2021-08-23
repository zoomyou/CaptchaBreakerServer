package com.example.captchabreaker.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("aijob")
public class Job {

    private String jobid;
    private String src_type;
    private String captcha_data;
    private String bypassresult;
    private String user_id;
    private String path;
    private String catecode;
    private Timestamp date;

    public Job(String jobid, String src_type, String captcha_data){
        this.setJobid(jobid);
        this.setSrc_type(src_type);
        this.setCaptcha_data(captcha_data);
        this.setUser_id("418");
    }
}
