package org.TPDesarrollo.clases;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estadia", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estadia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estadia")
    private int idestadia;

    @Column(name = "fecha_ingreso")
    private String fechaIngreso;

    @Column(name = "fecha_egreso")
    private String fechaEgreso;
}