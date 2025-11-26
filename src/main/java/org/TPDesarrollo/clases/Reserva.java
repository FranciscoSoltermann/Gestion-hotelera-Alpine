package org.TPDesarrollo.clases;

import jakarta.persistence.*;
import org.TPDesarrollo.enums.EstadoHabitacion; // Asegúrate de importar tu Enum

import java.time.LocalDate;

@Entity
@Table(name = "reserva", schema = "pruebabdd")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer id_reserva;

    @Column(name = "ingreso")
    private LocalDate ingreso;

    @Column(name = "egreso")
    private LocalDate egreso;

    @Column(name = "id_persona")
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

    // --- GETTERS QUE FALTABAN PARA EL GESTOR ---

    // Estos métodos actúan como alias para que tu GestorHabitacion no falle
    // al llamar a getFechaIngreso()
    public LocalDate getFechaIngreso() {
        return this.ingreso;
    }

    public LocalDate getFechaEgreso() {
        return this.egreso;
    }

    // Método lógico para determinar el estado.
    // Si la reserva existe y está activa, asumimos que la habitación está 'OCUPADA' o 'RESERVADA'.
    public EstadoHabitacion getEstadoHabitacion() {
        // Puedes cambiar esto a RESERVADA si prefieres mostrar ese color
        return EstadoHabitacion.OCUPADA;
    }
}