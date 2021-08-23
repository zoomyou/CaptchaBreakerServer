package com.example.captchabreaker.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableAutoConfiguration
public class GlobalConfig {

    @Value("${available.user.getTime}")
    private int availableUserGetTime;

    @Value("${answer.getTime}")
    private int answerGetTime;

    @Value("${time.slot}")
    private int timeSlot;

    @Value("${saveUrlDataUrl}")
    private String saveUrlDataUrl;

    @Value("${saveBase64DataUrl}")
    private String saveBase64DataUrl;

    @Value("${savePhotoPath}")
    private String savePhotoPath;

    @Value("${photoClassifyUrl}")
    private String photoClassifyUrl;

    @Value("${charactorRecognition}")
    private String charactorRecognition;

    @Value("${autoRecognitionUrl}")
    private String autoRecognitionUrl;

    @Value("${reCaptcha3Recognition}")
    private String reCaptcha3Recognition;

    @Value("${reCaptcha4Recognition}")
    private String reCaptcha4Recognition;

    @Value("${moveRecognition}")
    private String moveRecognition;
}
