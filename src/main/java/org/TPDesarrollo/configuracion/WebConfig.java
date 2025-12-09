package org.TPDesarrollo.configuracion;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de CORS (Cross-Origin Resource Sharing) para la aplicación.
 * Esencial para permitir la comunicación entre el front-end y el back-end.
 * Permite solicitudes desde el front-end de desarrollo (http://localhost:3000)
 * y habilita el intercambio de credenciales/cookies de sesión.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica las reglas a todas las rutas de la API

                // Especifica el origen exacto de tu front-end
                .allowedOrigins("http://localhost:3000")

                // Métodos HTTP necesarios para la API
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")

                // Permite todos los encabezados
                .allowedHeaders("*")

                // CRÍTICO para la Autenticación: Permite que las cookies de sesión
                // se envíen desde el front-end.
                .allowCredentials(true);
    }
}