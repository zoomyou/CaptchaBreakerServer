package com.example.captchabreaker.bean;

import lombok.Data;

@Data
public class User {

    private int user_id;
    private String user_name;
    private String password;
    private String mail;
    private String role;
    private String status;
    private String token;
    private double mark;
    private String hasPhone;
    private String phone;

}
