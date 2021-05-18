package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry
                .addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        registry
                .addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry
                .addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/");
        registry
                .addResourceHandler("/fonts/**")
                .addResourceLocations("classpath:/static/fonts/");
        registry
                .addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry
                .addResourceHandler("/post/upload/**")
                .addResourceLocations("file:upload/");
        registry
                .addResourceHandler("/posts/upload/**")
                .addResourceLocations("file:upload/");
        registry
                .addResourceHandler("/my/upload/**")
                .addResourceLocations("file:upload/");
        registry
                .addResourceHandler("/upload/**")
                .addResourceLocations("file:upload/");
    }

}