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

    // Este es el ID del titular (quien paga/hace la reserva)
    @Column(name = "id_persona")
    private Integer idPersona;

    @Column(name = "estado")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_habitacion")
    private Habitacion habitacion;

    // --- NUEVO: LISTA DE OCUPANTES ---
    // mappedBy = "reserva" se refiere al atributo 'reserva' en la clase Ocupante
    // CascadeType.ALL: Si guardas la reserva, se guardan los ocupantes.
    // orphanRemoval = true: Si borras un ocupante de la lista, se borra de la BD.
    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ocupante> ocupantes = new ArrayList<>();

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

    // --- GETTERS Y SETTERS PARA OCUPANTES ---

    public List<Ocupante> getOcupantes() {
        return ocupantes;
    }

    public void setOcupantes(List<Ocupante> ocupantes) {
        this.ocupantes = ocupantes;
    }

    // Método helper para mantener la consistencia bidireccional
    public void agregarOcupante(Ocupante ocupante) {
        if (ocupante != null) {
            ocupante.setReserva(this); // Vincula el ocupante a esta reserva
            this.ocupantes.add(ocupante);
        }
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