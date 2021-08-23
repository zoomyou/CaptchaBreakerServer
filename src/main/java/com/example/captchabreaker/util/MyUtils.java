package com.example.captchabreaker.util;

public class MyUtils {

    public static String getOther(String image){
        int index = image.indexOf(',');
        return image.substring(index+1);
    }

    public static void main(String[] args) {
        String a = "fd,safda";
        System.out.println(getOther(a));
    }
}
