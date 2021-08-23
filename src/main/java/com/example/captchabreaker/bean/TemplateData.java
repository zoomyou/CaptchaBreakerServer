package com.example.captchabreaker.bean;

import lombok.Data;

@Data
public class TemplateData {

    private String value;

    public TemplateData(String value){
        this.value = value;
    }

}
