// src/main/java/org/TPDesarrollo/MiProyecto.java
package org.TPDesarrollo;

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
