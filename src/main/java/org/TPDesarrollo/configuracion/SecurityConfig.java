package org.TPDesarrollo.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración de seguridad para la aplicación.
 * Define las reglas de acceso y los mecanismos de autenticación.
 * Utiliza Spring Security para gestionar la seguridad de las rutas de la API.
 * Permite el acceso libre a las rutas de usuario, huésped, reserva y habitación,
 * mientras que protege cualquier otra ruta que requiera autenticación.
 * También configura un codificador de contraseñas utilizando BCrypt.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usuarios/**").permitAll()
                        .requestMatchers("/api/huespedes/**").permitAll()
                        .requestMatchers("/api/reservas/**").permitAll()
                        .requestMatchers("/api/habitaciones/**").permitAll()
                        .requestMatchers("/api/facturas/**").permitAll()
                        .requestMatchers("/api/responsables/**").permitAll()
                        .requestMatchers("/api/consumos/**").permitAll()
                        .requestMatchers("/api/pagos/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}