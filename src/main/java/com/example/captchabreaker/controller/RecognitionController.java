package com.example.captchabreaker.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.captchabreaker.bean.Job;
import com.example.captchabreaker.bean.Miniuser;
import com.example.captchabreaker.config.GlobalConfig;
import com.example.captchabreaker.mapper.AiJobMapper;
import com.example.captchabreaker.mapper.MiniuserMapper;
import com.example.captchabreaker.service.*;
import com.example.captchabreaker.util.MyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
public class RecognitionController {

    @Autowired
    GlobalConfig globalConfig;
    @Autowired
    AvailableUserService availableUserService;
    @Autowired
    ResultMapService resultMapService;
    @Autowired
    SendWXMessageService sendWXMessageService;
    @Autowired
    MiniuserMapper miniuserMapper;
    @Autowired
    ImgSaveService imgSaveService;
    @Autowired
    ClassifyService classifyService;
    @Autowired
    AutoRecognizeService autoRecognizeService;
    @Autowired
    AiJobMapper aiJobMapper;
    @Autowired
    AutoClassifyService autoClassifyService;
    @Autowired
    ThreeService threeService;

    @PostMapping("/recognition")
    public JSONObject recognition(@RequestBody JSONObject params, @RequestHeader("token") String token){
        log.info("任务接收开始...");
        JSONObject res = new JSONObject();
        String status = "500", message = "系统出错", result = null, id = null;

        Job job = new Job(UUID.randomUUID().toString(), params.getString("src_type"), params.getString("data"));
        String src_type = params.getString("src_type");

        if (src_type == "5"){
            String threeRes = threeService.getRes(null, MyUtils.getOther(params.getString("data")), job.getJobid());
            if (threeRes != null){
                res.put("status", "200");
                res.put("message", "第三方识别成功");
                res.put("bypass_result", threeRes);
                return res;
            }
        }

        String path = globalConfig.getSavePhotoPath() + job.getJobid() + ".png";
        job.setPath(path);
        // 先保存成功再进行自动识别
        if (imgSaveService.save(params, path)){
            log.info("向 {} 保存图片成功", path);
            job.setPath(path);
            // 只有base64和图片链接能够成功
            if (src_type.equals("1") || src_type.equals("2")){
                String classRes = classifyService.classify(path);
                if (classRes.equals("0")){
                    job.setCatecode("1001");
                }
                if (classRes.equals("1")){
                    job.setCatecode("1202");
                }
                if (classRes.equals("2")){
                    job.setCatecode("1201");
                }
                if (classRes.equals("3")){
                    job.setCatecode("1101");
                }
                JSONObject autoRes = null;
                try {
                    autoRes = autoClassifyService.autoRecognition(classRes, path);
                    if (autoRes.getString("status").equals("200")){

                        res.put("bypass_result", autoRes.getString("bypass_result"));
                        res.put("message", "自动识别成功");
                        res.put("status", "200");
                        job.setBypassresult(res.getString("bypass_result"));
                        job.setDate(new Timestamp(System.currentTimeMillis()));
                        aiJobMapper.insert(job);
                        return res;
                    }
                } catch (Exception e) {
                    log.error("自动识别失败将开启手动识别！");
                }

            }
        }

        List<Miniuser> miniuserList = miniuserMapper.selectList(null);
        for (Miniuser miniuser : miniuserList) {
            JSONObject pushResult = sendWXMessageService.captchaPush(miniuser.getOpenid(), params.getString("src_type"));
            log.info(pushResult.toJSONString());
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 获取空闲用户
        id = availableUserService.appointUser(params.getString("src_type"));

        log.info("类型 {} 最终获取的空闲用户为：{}", params.getString("src_type"), id);

        if (id == null){
            // 没有获取到空闲用户
            message = "短时间内无空闲用户在线";
            result = "";
        } else {
            boolean flag = false;

            // 将任务封装为任务对象
            JSONObject jobMessage = new JSONObject();
            jobMessage.put("src_type", job.getSrc_type());
            jobMessage.put("show_data", job.getCaptcha_data());
            jobMessage.put("cate_code", "0");

            log.info("向客户端推送的数据为：{}",jobMessage.getString("src_type"));

            // 向用户推送打码任务
            try {
                flag = WebSocketServer.sendMessage(JSONObject.parseObject(JSON.toJSONString(jobMessage)), id, job.getJobid());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!flag){
                // 消息推送失败
                log.info("向用户 {} 推送消息失败。", id);
                message = "任务推送失败";
            } else {
                // 推送消息成功
                log.info("向用户 {} 推送消息成功。", id);

                // 开始查询任务结果
                result = resultMapService.finalResult(job.getJobid());

                log.info("查询到的结果为：{}", result);

                // 对结果进行判断
                if (result != null){
                    if (result.equals("giveUp") || result.equals("giveup")){
                        message = "打码端放弃打码！";
                    } else if (result.equals("timeUp") || result.equals("timeout")){
                        message = "打码端超时！";
                    } else if (result.equals("closeWebSocket")){
                        message = "打码端意外关闭！";
                    } else {
                        status = "200";
                        message = "打码成功！";
                    }
                } else {
//                    availableUserService.add(id);
//                    availableUserService.delete(id);
                    message = "打码端放弃或超时！";
                }
            }
        }

        res.put("status", status);
        res.put("message", message);
        res.put("bypass_result", result == null ? "" : result.trim());

        return res;
    }

}
