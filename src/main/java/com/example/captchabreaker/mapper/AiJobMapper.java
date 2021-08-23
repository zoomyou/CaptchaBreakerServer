package com.example.captchabreaker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.captchabreaker.bean.Job;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AiJobMapper extends BaseMapper<Job>{
}
