package org.TPDesarrollo.clases;

import jakarta.persistence.*;
import org.TPDesarrollo.enums.EstadoHabitacion;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una reserva en el sistema de gestión hotelera.
 * Esta clase está mapeada a la tabla "reserva" en el esquema "pruebabdd" de la base de datos.
 * Contiene información sobre las fechas de ingreso y egreso,
 * el ID de la persona que realiza la reserva, el estado de la reserva,
 * la habitación asociada y la lista de ocupantes de la reserva.
 * Además, incluye métodos para gestionar la relación con los ocupantes.
 */
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

    // Este es el ID del titular (quien paga/hace la reserva)
    @Column(name = "id_persona")
    private Integer idPersona;

    @Column(name = "estado")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_habitacion")
    private Habitacion habitacion;


    // mappedBy = "reserva" se refiere al atributo 'reserva' en la clase Ocupante
    // CascadeType.ALL: Si guardas la reserva, se guardan los ocupantes.
    // orphanRemoval = true: Si borras un ocupante de la lista, se borra de la BD.
    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ocupante> ocupantes = new ArrayList<>();

    public void agregarOcupante(Ocupante ocupante) {
        ocupantes.add(ocupante);
        ocupante.setReserva(this);
    }
    public Reserva() {}

    //GETTERS Y SETTERS

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

    //GETTERS Y SETTERS

    public List<Ocupante> getOcupantes() {
        return ocupantes;
    }

    public void setOcupantes(List<Ocupante> ocupantes) {
        this.ocupantes = ocupantes;
    }

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