package org.TPDesarrollo.clases;

import jakarta.persistence.*;
import org.TPDesarrollo.enums.EstadoHabitacion;

import java.time.LocalDate;
import java.util.ArrayList; // No olvides importar ArrayList
import java.util.List;

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

    // --- ¡AQUÍ ESTÁ LA CORRECCIÓN! ---
    // Debes declarar la lista y poner las anotaciones sobre ella.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "reserva_habitacion",
            joinColumns = @JoinColumn(name = "id_reserva"),
            inverseJoinColumns = @JoinColumn(name = "id_habitacion")
    )
    private List<Habitacion> habitaciones = new ArrayList<>();


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

    // --- MÉTODOS AUXILIARES ---

    public LocalDate getFechaIngreso() {
        return this.ingreso;
    }

    public LocalDate getFechaEgreso() {
        return this.egreso;
    }

    public EstadoHabitacion getEstadoHabitacion() {
        return EstadoHabitacion.OCUPADA;
    }

    // --- GETTER Y SETTER PARA LA LISTA ---

    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(List<Habitacion> habitaciones) {
        this.habitaciones = habitaciones;
    }
}