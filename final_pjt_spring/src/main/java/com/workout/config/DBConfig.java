package com.workout.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.workout.model.dao")
public class DBConfig {
	
}
