package com.workout.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	@Bean // 스프링 빈으로 등록합니다.
	public OpenAPI openAPI() {
		return new OpenAPI().components(new Components()) // API 구성 요소를 설정합니다.
				.info(apiInfo()); // API 정보를 설정합니다.
	}

	private Info apiInfo() {
		return new Info().title("금지 가롱의 관통 프로젝트") // API 제목을 설정합니다.
				.description("<h3>우당탕탕 관통일기</h3>") // API 설명을 설정합니다.
				.version("1.0.0"); // API 버전을 설정합니다.
	}

}
