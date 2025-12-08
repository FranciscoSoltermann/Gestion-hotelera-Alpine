package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.TPDesarrollo.enums.RazonSocial;

import java.time.LocalDate;

@Entity
@Table(name = "huesped", schema = "pruebabdd")
@PrimaryKeyJoinColumn(name = "id_persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Entidad que representa a un huésped o cliente registrado. Hereda datos básicos de Persona.")
public class Huesped extends Persona {

    @Column(unique = true)
    @Schema(description = "Clave Única de Identificación Tributaria (CUIT). Debe ser único.", example = "20123456789")
    private String cuit;

    @Column(name = "ocupacion")
    @Schema(description = "Ocupación o profesión del huésped.", example = "Ingeniero")
    private String ocupacion;

    @Column(name = "nacionalidad")
    @Schema(description = "Nacionalidad del huésped.", example = "Argentino")
    private String nacionalidad;

    @Column(name = "fecha_nac")
    @Schema(description = "Fecha de nacimiento del huésped (Formato ISO: YYYY-MM-DD).", example = "1990-01-20")
    private LocalDate fechaNacimiento;

    @Column(name = "pos_iva")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Posición del huésped frente al IVA (ej: CONSUMIDOR_FINAL).", example = "CONSUMIDOR_FINAL")
    private RazonSocial posicionIVA;

    @Column(name = "email")
    @Schema(description = "Correo electrónico de contacto.", example = "usuario@mail.com")
    private String email;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_direccion")
    @Schema(description = "Referencia a la dirección de residencia del huésped.")
    private Direccion direccion;
}