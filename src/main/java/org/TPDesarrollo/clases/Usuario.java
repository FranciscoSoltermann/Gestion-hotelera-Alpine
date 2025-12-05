package org.TPDesarrollo.clases;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario", schema = "pruebabdd")
@Getter
@Setter
@NoArgsConstructor  // Constructor vacío obligatorio para JPA
@AllArgsConstructor // Constructor con todos los campos (necesario para @Builder)
@Builder            // Crea el patrón Builder automáticamente (incluyendo ID)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "contrasenia", nullable = false)
    private String contrasenia;

    @Column(name = "rol")
    private String rol;
}