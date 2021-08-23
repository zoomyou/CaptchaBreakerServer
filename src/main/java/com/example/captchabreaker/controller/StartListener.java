package com.example.captchabreaker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import java.util.Set;

@Slf4j
@Component
public class StartListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    Set<String> webOnlineUserList;
    @Autowired
    Set<String> miniProOnlineUserList;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        log.info("项目已启动，开始测试redis、MySQL和RabbitMQ连接，并清空redis......");
        if (webOnlineUserList == miniProOnlineUserList){
            log.info("ok");
        } else {
            log.info("no");
        }

    }
}
