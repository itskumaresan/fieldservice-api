package com.gaksvytech.fieldservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry
			.addMapping("/**")
			//.allowedOrigins("*") -- If undefined, all origins are allowed
			//.allowedHeaders("*") -- If undefined, all requested headers are allowed
			.allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
			.allowCredentials(true);
	}

}
