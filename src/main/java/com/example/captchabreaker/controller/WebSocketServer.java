package com.example.captchabreaker.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.captchabreaker.service.AvailableUserService;
import com.example.captchabreaker.service.ResultMapService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint("/socket/{userType}")
@Component
@Slf4j
@Data
public class WebSocketServer {

    // 存放所有在线的客户端的映射
    private static Map<String, WebSocketServer> clients = new ConcurrentHashMap<>();
    // 会话session
    private Session session;
    // 标识字符串
    private String id = "";
    // 被分配到的
    private String jobId = "";
    // 类型（小程序和web端）
    private String type = "";

    // 需要注入的类为空闲用户服务类
    private static ResultMapService resultMapService;
    private static AvailableUserService availableUserService;

    @Autowired
    public void setIndependcy(ResultMapService resultMapService, AvailableUserService availableUserService){
        WebSocketServer.resultMapService = resultMapService;
        WebSocketServer.availableUserService = availableUserService;
    }


    @OnOpen
    public void onOpen(@PathParam("userType") String type, Session session){
        // 随机生成一个全局的唯一标识
        String tempId = UUID.randomUUID().toString();
        // 将该websocket连接存入到映射中以供取用
        clients.put(tempId, this);

        log.info("连接socket的用户的类型是："+type);

        // 设置本websocket对象的id和session信息
        setId(tempId);
        setSession(session);
        setType(type);

        if (getType().equals("mini")){
            log.info("有新的打码客户端连接了：{}，是小程序用户。", tempId);
        } else {
            log.info("有新的打码客户端连接了：{}，是网页端用户。", tempId);
        }

        // 将其加入到空闲用户列表中
        availableUserService.add(tempId, getType());
    }

    @OnClose
    public void onClose(){
        // 将映射移除
        clients.remove(getId());

        log.info("有用户断开了，id为：{}", getId());

        // 将用户从在线空闲用户列表中删除
        availableUserService.delete(getId(), getType());

        // 如果还有任务在执行则直接返回错误
        if (!getJobId().equals("")){
            resultMapService.add(getJobId(), "closeWebSocket");
        }
    }

    @OnError
    public void onError(Throwable throwable){
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message) throws IOException {
        log.info("服务端收到客户端发来的消息：{}，且其对应的任务id为：{}", message.hashCode(), getJobId());

        // 收到消息后对任务结果进行映射，将任务id清空，并将其加入空闲用户列表中
        resultMapService.add(getJobId(), message);
        setJobId("");
        availableUserService.add(getId(), type);
    }

    private void sendMessage(String message, String jobId) throws IOException{
        // 向本websocket连接发送消息
        this.session.getBasicRemote().sendText(message);

        // 设置本对象的任务id
        setJobId(jobId);

        log.info("向用户 {} 发送了任务 {}", getId(), getJobId());
    }

    public static boolean sendMessage(JSONObject object, String id, String jobid) throws IOException{
        // 保证用户存在
        if (id !=null && !id.equals("")){
            // 如果全局映射中存在该用户则取出并向其发送消息
            if (clients.containsKey(id)){
                clients.get(id).sendMessage(object.toJSONString(), jobid);
                return true;
            }
        }

        return false;
    }
}
