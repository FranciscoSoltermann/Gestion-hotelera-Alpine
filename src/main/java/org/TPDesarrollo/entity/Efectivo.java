package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
/**
 * Clase que representa un medio de pago en efectivo.
 * Hereda de la clase MedioDePago.
 * Esta clase está mapeada a la tabla "efectivo" en el esquema "pruebabdd" de la base de datos.
 * No contiene atributos adicionales, pero puede ser extendida en el futuro para incluir
 * características específicas de los pagos en efectivo.
 */
@Entity
@Table(name = "efectivo", schema = "pruebabdd")
@Data
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Entidad que representa un pago realizado en efectivo. No contiene atributos propios, solo hereda los campos comunes de MedioDePago.")
public class Efectivo extends MedioDePago {

}