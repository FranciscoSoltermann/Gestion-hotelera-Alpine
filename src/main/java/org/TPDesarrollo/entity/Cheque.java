package org.TPDesarrollo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "cheque", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Cheque extends MedioDePago {

    @Column(name = "numero_cheque")
    private int numeroCheque;

    @Column(name = "fecha_cheque")
    private LocalDate fechaCheque;

    @Column(name = "plaza")
    private String Plaza;

    @ManyToOne
    @JoinColumn(name = "id_banco")
    private Banco banco;
}