package com.workout.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@MapperScan("com.workout.model.dao")
@Configuration
public class MybatisConfig {

}
