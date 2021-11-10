package com.white.springcloud.alibaba.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.white.springcloud.alibaba.dao"})
public class MyBatisConfig {

}
