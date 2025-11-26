package org.TPDesarrollo.clases;

import jakarta.persistence.*;
import org.TPDesarrollo.enums.EstadoHabitacion;

import java.time.LocalDate;
import java.util.ArrayList;
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

    // --- CORRECCIÓN IMPORTANTE ---
    // Una reserva tiene MUCHAS habitaciones
    // mappedBy = "reservaActual" referencia el campo en Habitacion
    @OneToMany(mappedBy = "reservaActual", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

    public LocalDate getFechaIngreso() {
        return this.ingreso;  // ahora coincide con el atributo real
    }

    public LocalDate getFechaEgreso() {
        return this.egreso;   // ahora coincide con el atributo real
    }

    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(List<Habitacion> habitaciones) {
        this.habitaciones = habitaciones;
    }

    public void agregarHabitacion(Habitacion habitacion) {
        this.habitaciones.add(habitacion);
        habitacion.setReservaActual(this);
    }

    public EstadoHabitacion getEstadoHabitacion() {

        // Si la reserva aplica a varias habitaciones,
        // podemos decir que mientras exista una habitación ocupada,
        // la reserva "está ocupando".

        if (habitaciones == null || habitaciones.isEmpty()) {
            return EstadoHabitacion.DISPONIBLE;
        }

        // Si alguna habitación asociada está ocupada → la reserva está ocupada
        boolean algunaOcupada = habitaciones.stream()
                .anyMatch(h -> h.getEstado() == EstadoHabitacion.OCUPADA);

        return algunaOcupada ? EstadoHabitacion.OCUPADA : EstadoHabitacion.DISPONIBLE;
    }
}
