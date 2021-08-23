package com.example.captchabreaker.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;


@Data
@Component
@EnableAutoConfiguration
public class MiniProConfig {

    @Value("${miniProgram.auth.code2Session}")
    private String miniProgramAuthCode2Session;

    @Value("${miniProgram.appid}")
    private String miniProgramAppid;

    @Value("${miniProgram.secret}")
    private String miniProgramSecret;

    @Value("${miniProgram.grant_type}")
    private String miniProgramGrantType;

    @Value("${miniProgram.token}")
    private String miniProgramToken;

    @Value("${miniProgram.auth.getAccessToken}")
    private String miniProgramAuthGetAccessToken;

    @Value("${miniProgram.subscribeMessage.send}")
    private String miniProgramSubscribeMessageSend;

}
