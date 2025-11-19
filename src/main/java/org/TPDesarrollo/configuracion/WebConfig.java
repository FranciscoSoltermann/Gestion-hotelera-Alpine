// src/main/java/org/TPDesarrollo/config/WebConfig.java
package org.TPDesarrollo.configuracion;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Esto le da permiso a CUALQUIER ruta en tu API
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // Permite tu front-end
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite el POST y el OPTIONS
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}