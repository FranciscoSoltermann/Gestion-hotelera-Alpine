package org.TPDesarrollo.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Tu PasswordEncoder (ya lo tenías, déjalo así)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desactivamos CSRF (necesario para que funcionen los POST desde Postman/React en este tipo de API)
                .csrf(AbstractHttpConfigurer::disable)

                // Configuramos las reglas de las URL
                .authorizeHttpRequests(auth -> auth
                        // Permitimos entrar a TODOS a las rutas de usuario (login, registrar)
                        .requestMatchers("/api/usuarios/**").permitAll()
                        .requestMatchers("/api/huespedes/**").permitAll()


                        .anyRequest().authenticated()
                );

        return http.build();
    }
}