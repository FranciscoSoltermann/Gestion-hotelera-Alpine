package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario", schema = "pruebabdd")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que almacena las credenciales de un usuario para autenticación y autorización.")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    @Schema(description = "ID único del usuario.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    @Schema(description = "Nombre de usuario único para el login.", example = "admin.hotel", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Column(name = "contrasenia", nullable = false)
    @Schema(description = "Contraseña cifrada (hashed).", example = "MiPass1234", accessMode = Schema.AccessMode.WRITE_ONLY, requiredMode = Schema.RequiredMode.REQUIRED)
    private String contrasenia;

    @Column(name = "rol")
    @Schema(description = "Rol del usuario en el sistema (ej: 'ADMIN', 'RECEPCIONISTA').", example = "ADMIN")
    private String rol;
}