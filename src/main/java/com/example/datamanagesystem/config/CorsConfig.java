package com.example.datamanagesystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
	@Bean
	public CorsFilter corsFilter() {
		// 1. 配置跨域
		CorsConfiguration config = new CorsConfiguration();
		// 允许的前端域名（本地开发填 *，生产环境填具体域名）
		config.addAllowedOriginPattern("*");
		// 允许的请求头
		config.addAllowedHeader("*");
		// 允许的请求方法（GET/POST/DELETE等）
		config.addAllowedMethod("*");
		// 允许携带Cookie
		config.setAllowCredentials(true);
		// 预检请求的有效期（单位：秒）
		config.setMaxAge(3600L);

		// 2. 配置路径匹配规则
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/api/**", config); // 对所有/api开头的接口生效

		// 3. 返回CorsFilter
		return new CorsFilter(source);
	}
}