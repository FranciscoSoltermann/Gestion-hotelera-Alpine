package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "efectivo", schema = "pruebabdd")
@Data
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Entidad que representa un pago realizado en efectivo. No contiene atributos propios, solo hereda los campos comunes de MedioDePago.")
public class Efectivo extends MedioDePago {

}