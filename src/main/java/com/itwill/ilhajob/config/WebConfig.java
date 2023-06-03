package com.itwill.ilhajob.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.hibernate5.support.OpenSessionInViewInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwill.ilhajob.user.controller.AuthLoginAnnotationInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	/********************WebMvcConfigurer재정의*********************
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index.jsp");
	}
	*************************************************************/
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		AuthLoginAnnotationInterceptor annotationInterceptor = new AuthLoginAnnotationInterceptor();
		registry.addInterceptor(annotationInterceptor)
		.addPathPatterns("/**")
		.excludePathPatterns("/css/**")
		.excludePathPatterns("/js/**")
		.excludePathPatterns("/images/**");
		
	}
	/*********************Spring MVC 빈객체등록*********************/
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	String os = System.getProperty("os.name").toLowerCase();
        String projectPath = System.getProperty("user.dir");
    	
        registry.addResourceHandler("/upload/logo/**")
            .addResourceLocations("file:C://final-project-team1-ilhajob//upload//logo//")
            .setCachePeriod(0);
        registry.addResourceHandler("/upload/profile/**")
	        .addResourceLocations("file:C://final-project-team1-ilhajob//upload//profile//")
	        .setCachePeriod(0);
        registry.addResourceHandler("/upload/blog/**")
	        .addResourceLocations("file:C://final-project-team1-ilhajob//upload//blog//")
	        .setCachePeriod(0);
        
        // Mac 경로
        if (os.contains("mac")) {
            registry.addResourceHandler("/upload/logo/**")
                .addResourceLocations("file:" + projectPath + "/upload/logo/")
                .setCachePeriod(0);
            registry.addResourceHandler("/upload/profile/**")
            	.addResourceLocations("file:" + projectPath + "/upload/profile/")
                .setCachePeriod(0);
            registry.addResourceHandler("/upload/blog/**")
	            .addResourceLocations("file:" + projectPath + "/upload/blog/")
                .setCachePeriod(0);
        }
        
    }
}
