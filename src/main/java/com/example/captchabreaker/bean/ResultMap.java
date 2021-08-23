package com.example.captchabreaker.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ResultMap {

    @Bean(name = "results")
    public Map<String, String> results(){
        Map<String, String> result = new ConcurrentHashMap<>();
        return result;
    }

}
