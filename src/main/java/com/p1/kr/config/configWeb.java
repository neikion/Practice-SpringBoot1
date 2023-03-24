package com.p1.kr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

@Configuration
public class configWeb implements WebMvcConfigurer {
	@Bean
	public ClassLoaderTemplateResolver templateResolver() {
		
		var resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("templates/");
		resolver.setSuffix(".html");
		resolver.setCacheable(true);
		resolver.setTemplateMode("html");
		resolver.setCharacterEncoding("UTF-8");
		return resolver;
	}
	@Bean
	public SpringTemplateEngine Engine() {
		var engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver());
		engine.addDialect(new LayoutDialect());
		return engine;
	}
	@Bean
	public ViewResolver viewResolver() {
		var viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(Engine());
		viewResolver.setCharacterEncoding("UTF-8");
		return viewResolver;
	}
	
	public void addController(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index.html");
	}
}
