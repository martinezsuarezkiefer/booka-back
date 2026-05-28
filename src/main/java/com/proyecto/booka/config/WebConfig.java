package com.proyecto.booka.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @SuppressWarnings("null")
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
            .allowedOrigins(
                "http://localhost:4200",
                "https://booka-front.vercel.app"
            )
            .allowedMethods(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS"
            )
            .allowedHeaders("*")
            .exposedHeaders("Authorization")
            .allowCredentials(true);
    }

    @SuppressWarnings("null")
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // IMPORTANTE: no quitar el mapeo default
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
