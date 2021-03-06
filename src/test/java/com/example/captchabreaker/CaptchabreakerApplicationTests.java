package com.example.captchabreaker;

import com.alibaba.fastjson.JSONObject;
import com.example.captchabreaker.bean.Job;
import com.example.captchabreaker.controller.AiLogController;
import com.example.captchabreaker.mapper.AiJobMapper;
import com.example.captchabreaker.service.ImgSaveService;
import com.example.captchabreaker.service.ThreeService;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class CaptchabreakerApplicationTests {

	@Autowired
	AiJobMapper aiJobMapper;
	@Autowired
	ImgSaveService imgSaveService;
	@Autowired
	AiLogController aiLogController;
	@Autowired
	ThreeService threeService;

	@Test
	void contextLoads() {
		Job job = new Job("dfafas", "1", "fkdaj;flksjd;af;sld");
		//System.out.println(aiJobMapper.insert(job));
		Map<String, Object> map = new HashMap<>();
		map.put("jobid", "1");
		System.out.println(aiJobMapper.selectByMap(map));
	}

	@Test
	void aitest(){
		Map<String, Object> map = new HashMap<>();
		map.put("jobid", "1");
		List<Job> list = aiJobMapper.selectByMap(map);
		System.out.println(list.get(0));
	}

	@Test
	void saveTest(){
		boolean flag = imgSaveService.urlSave("https://img0.baidu.com/it/u=2151136234,3513236673&fm=26&fmt=auto&gp=0.jpg","/home/BitProject/Pic/fdas.png");
		System.out.println(flag);
	}

	@Test
	void addlog(){
		String image = "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";
		String s = threeService.getRes(null, image, "fdsfdsfa");
		System.out.println(s);
	}

}
