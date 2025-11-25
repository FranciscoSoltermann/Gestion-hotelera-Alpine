package org.TPDesarrollo.clases;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reserva", schema = "pruebabdd") // Agregué el esquema por si acaso
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer id_reserva;

    @Column(name = "ingreso") // Según tu foto
    private LocalDate ingreso;

    @Column(name = "egreso") // Según tu foto
    private LocalDate egreso;

    @Column(name = "id_persona") // Según tu foto
    private Integer idPersona;

    public Reserva() {}

    // --- GETTERS Y SETTERS ---

    public Integer getId() { return id_reserva; }
    public void setId(Integer id) { this.id_reserva = id; }

    public LocalDate getIngreso() { return ingreso; }
    public void setIngreso(LocalDate ingreso) { this.ingreso = ingreso; }

    public LocalDate getEgreso() { return egreso; }
    public void setEgreso(LocalDate egreso) { this.egreso = egreso; }

    public Integer getIdPersona() { return idPersona; }
    public void setIdPersona(Integer idPersona) { this.idPersona = idPersona; }
}