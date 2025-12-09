package org.TPDesarrollo.configuracion;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración del bean ModelMapper.
 * * Se configura para asegurar la integridad de los datos durante el mapeo entre DTOs y Entidades,
 * cumpliendo con las mejores prácticas para aplicaciones que usan Lombok.
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Configura y proporciona una instancia de ModelMapper con las siguientes características:
     * Estrategia de coincidencia STRICT para asegurar que los nombres de los
     * campos de origen y destino coincidan exactamente.
     * Habilita el Field Matching y el acceso a campos privados, esencial cuando se usa Lombok (@Data).
     * @return una instancia configurada de ModelMapper.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // 1. Estrategia STRICT: Asegura que los nombres de los campos de origen y destino
        // deben coincidir exactamente. Esto previene errores de mapeo silenciosos.
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        // 2. Habilitar Field Matching y acceso a campos privados:
        // Esencial cuando se usa Lombok (@Data) para que ModelMapper acceda
        // a los campos directamente sin depender de los métodos 'getter' y 'setter'.
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        return modelMapper;
    }
}