package com.example.captchabreaker.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.captchabreaker.bean.Job;
import com.example.captchabreaker.mapper.AiJobMapper;
import com.example.captchabreaker.service.ImgToBase64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class AiLogController {

    @Autowired
    AiJobMapper aiJobMapper;
    @Autowired
    ImgToBase64 imgToBase64;

    @GetMapping("/tasklogs")
    public JSONObject getTaskLogs(){
        JSONObject res = new JSONObject();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", "418");
        List<Job> list = aiJobMapper.selectByMap(map);
        if (list.size() > 40){
            list = list.subList(list.size()-40, list.size());
        }

        List<JSONObject> resList = new ArrayList<>();
        for (Job job : list) {
            JSONObject temp = new JSONObject();
            temp.put("job_id", job.getJobid());
            temp.put("src_type", job.getSrc_type());
            temp.put("user_id", job.getUser_id());
            temp.put("bypass_result", job.getBypassresult());
            temp.put("cate_code", job.getCatecode()==null?null: Integer.parseInt(job.getCatecode()));
            temp.put("date", job.getDate());
            resList.add(temp);
        }

        res.put("data", resList);

        return res;
    }

    @GetMapping("/taskimg/{job_id}")
    public JSONObject getImgBase64(@PathVariable("job_id") String job_id){
        Map<String, Object> map = new HashMap<>();
        log.info(job_id);
        map.put("jobid", job_id);
        List<Job> list = aiJobMapper.selectByMap(map);
        log.info("get aijob :"+list.size()+" cap and object is :"+list.get(0).getPath());
        String base64 = null;
        try {
            base64 = imgToBase64.getBase64(list.get(0).getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject res = new JSONObject();
        res.put("img_base64", base64==null?list.get(0).getCaptcha_data():base64);
        return res;
    }
}
