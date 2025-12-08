package org.TPDesarrollo.configuracion;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BASIC_AUTH_SECURITY_SCHEME = "basicAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Hotel Premier API").version("1.0"))
                .components(new Components()
                        // 1. ESQUEMA DE SEGURIDAD PARA AUTENTICACIÓN BÁSICA
                        .addSecuritySchemes(BASIC_AUTH_SECURITY_SCHEME, createBasicSecurityScheme())

                        // 2. RESPUESTAS DE ERROR GLOBALES
                        .addResponses("BadRequest", createBadRequestResponse())
                        .addResponses("NotFound", createNotFoundResponse())
                        .addResponses("InternalError", createInternalErrorResponse()))

                // Aplicar el esquema de seguridad globalmente a todos los endpoints
                .addSecurityItem(new SecurityRequirement().addList(BASIC_AUTH_SECURITY_SCHEME));
    }

    private SecurityScheme createBasicSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic")
                .description("Autenticación básica HTTP (usuario y contraseña).")
                .in(SecurityScheme.In.HEADER);
    }

    // --- Definiciones de Respuestas de Error Comunes (para referenciar en Controladores) ---

    private ApiResponse createBadRequestResponse() {
        return new ApiResponse()
                .description("400: Petición incorrecta, error de validación (Jakarta Validation) o regla de negocio fallida.")
                .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().schema(new Schema<String>().example("{\"error\": \"Mensaje de error del Gestor\"}"))));
    }

    private ApiResponse createNotFoundResponse() {
        return new ApiResponse()
                .description("404: Recurso no encontrado (ej: ID, DNI o CUIT no existe en la base de datos).");
    }

    private ApiResponse createInternalErrorResponse() {
        return new ApiResponse()
                .description("500: Error inesperado del servidor (excepción no controlada).");
    }
}