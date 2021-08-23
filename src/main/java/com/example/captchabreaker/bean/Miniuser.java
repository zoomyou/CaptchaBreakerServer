package com.example.captchabreaker.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("miniuser")
public class Miniuser {

    private Integer miniid;
    private String openid;
    private Integer webid;

    public Miniuser(Integer miniid, String openid, Integer webid){
        this.miniid = miniid;
        this.openid = openid;
        this.webid = webid;
    }

}
