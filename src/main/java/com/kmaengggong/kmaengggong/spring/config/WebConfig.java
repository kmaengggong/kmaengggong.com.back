package com.kmaengggong.kmaengggong.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
			.addMapping("/**")
			.allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS")
			.allowedOrigins("http://localhost:3000");
		WebMvcConfigurer.super.addCorsMappings(registry);
	}
}
