package com.proyecto.booka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    @SuppressWarnings("null")
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization") 
                .allowCredentials(true);
    }

    @SuppressWarnings("null")
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Servir archivos desde C:/booka/uploads/
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);

        // IMPORTANTE: no quitar el mapeo default
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
