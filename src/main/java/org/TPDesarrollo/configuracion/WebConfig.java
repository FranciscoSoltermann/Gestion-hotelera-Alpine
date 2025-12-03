// src/main/java/org/TPDesarrollo/config/WebConfig.java
package org.TPDesarrollo.configuracion;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de CORS para la aplicación.
 * Permite solicitudes desde el front-end alojado en http://localhost:3000.
 * Configura los métodos HTTP permitidos y las cabeceras.
 * Esto es esencial para permitir la comunicación entre el front-end y el back-end.
 */
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