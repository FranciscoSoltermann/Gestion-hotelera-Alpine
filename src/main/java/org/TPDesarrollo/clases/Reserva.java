package org.TPDesarrollo.clases;

import jakarta.persistence.*;
import org.TPDesarrollo.enums.EstadoHabitacion; // O el enum que uses para estado reserva
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

    @Column(name = "estado")
    private String estado; // Para guardar "RESERVADA" o "OCUPADA"

    // --- CAMBIO CLAVE: La reserva sabe de qué habitación es ---
    @ManyToOne
    @JoinColumn(name = "id_habitacion") // Esto conecta con tu columna de la foto
    private Habitacion habitacion;

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

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }

    // Métodos auxiliares de fecha
    public LocalDate getFechaIngreso() { return ingreso; }
    public LocalDate getFechaEgreso() { return egreso; }


    public EstadoHabitacion getEstadoHabitacion() {
        try {
            return EstadoHabitacion.valueOf(this.estado);
        } catch (Exception e) {
            return EstadoHabitacion.DISPONIBLE;
        }
    }
}