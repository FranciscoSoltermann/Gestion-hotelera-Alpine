package org.TPDesarrollo;
/**
 * Clase principal de la aplicación Spring Boot.
 * Anotada con @SpringBootApplication para habilitar la configuración automática,
 * el escaneo de componentes y la configuración de Spring Boot.
 * También se habilitan los repositorios JPA en el paquete especificado.
 * Contiene el método main que inicia la aplicación.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.TPDesarrollo")
public class MiProyecto {
    public static void main(String[] args) {
        SpringApplication.run(MiProyecto.class, args);
    }
}
