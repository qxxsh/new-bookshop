package com.daniel.bookshop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置上传文件的静态资源访问
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:upload/");
        
        // 配置图片资源的静态资源访问
        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/");
        
        // 配置其他静态资源
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
} 