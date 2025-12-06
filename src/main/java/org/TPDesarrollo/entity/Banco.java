package org.TPDesarrollo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "banco", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_banco")
    private int idbanco;

    @Column(name = "nombre")
    private String nombre;
}