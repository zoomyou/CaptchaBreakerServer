package com.example.captchabreaker.service;

import com.example.captchabreaker.config.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ResultMapService {

    @Autowired
    Map<String, String> results;
    @Autowired
    GlobalConfig globalConfig;

    // 向其中加入结果
    public boolean add(String jobid, String res){
        String ans = results.put(jobid, res);
        // 如果结果为空就返回正确的
        if (ans == null){
            return true;
        }
        return false;
    }

    // 获取结果
    public String getResult(String jobid){
        String res = null;

        if (results.containsKey(jobid)){
            res = results.get(jobid);
            results.remove(jobid);
        }

        return res;
    }

    // 循环的获取结果
    public String finalResult(String jobid){
        String ans = null;
        int getAnswer = 0;
        while (ans == null && getAnswer<globalConfig.getAnswerGetTime()){
            ans = getResult(jobid);
            try {
                Thread.sleep(globalConfig.getTimeSlot());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getAnswer++;
        }
        return ans;
    }

}
