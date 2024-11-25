package com.workout.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import com.workout.interceptor.JwtInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	@Autowired
	private JwtInterceptor jwtInterceptor;

	
	@Override
<<<<<<< HEAD
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(jwtInterceptor)
	        .addPathPatterns("/**")
	        .excludePathPatterns("/api-user/**", "/swagger-ui/**", "/v3/api-docs/**", "/uploads/**");
	}

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
=======
   public void addResourceHandlers(ResourceHandlerRegistry registry) {
>>>>>>> vue
        // /uploads/** URL 요청을 실제 파일 시스템의 uploads 디렉토리로 매핑
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/") // 실제 파일 위치
                .setCachePeriod(3600) // 캐시 설정
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }

}
