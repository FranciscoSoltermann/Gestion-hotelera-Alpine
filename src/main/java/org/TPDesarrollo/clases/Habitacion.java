package org.TPDesarrollo.clases;

import jakarta.persistence.*;
import org.TPDesarrollo.enums.EstadoHabitacion;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "habitacion", schema = "pruebabdd")
public abstract class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_habitacion")
    private Integer id;

    @Column(name = "numero") // En la base de datos es Varchar
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoHabitacion estado;

    @Column(name = "costo")
    private Double costo;

    @Column(name = "capacidad")
    private Integer capacidad;

    @Column(name = "cama_ind")
    private Integer camaInd;

    @Column(name = "cama_doble")
    private Integer camaDoble;

    // --- CAMPOS NUEVOS SEGÚN TU FOTO ---
    @Column(name = "ingreso")
    private LocalDate ingreso;

    @Column(name = "egreso")
    private LocalDate egreso;

    // RELACIÓN: En tu base de datos, Habitacion tiene el id_reserva
    @OneToOne
    @JoinColumn(name = "id_reserva")
    private Reserva reservaActual;

    public Habitacion() {}

    // --- GETTERS Y SETTERS ---

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public EstadoHabitacion getEstado() { return estado; }
    public void setEstado(EstadoHabitacion estado) { this.estado = estado; }

    public Double getCosto() { return costo; }
    public void setCosto(Double costo) { this.costo = costo; }

    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }

    public Integer getCamaInd() { return camaInd; }
    public void setCamaInd(Integer camaInd) { this.camaInd = camaInd; }

    public Integer getCamaDoble() { return camaDoble; }
    public void setCamaDoble(Integer camaDoble) { this.camaDoble = camaDoble; }

    public LocalDate getIngreso() { return ingreso; }
    public void setIngreso(LocalDate ingreso) { this.ingreso = ingreso; }

    public LocalDate getEgreso() { return egreso; }
    public void setEgreso(LocalDate egreso) { this.egreso = egreso; }

    public Reserva getReservaActual() { return reservaActual; }
    public void setReservaActual(Reserva reservaActual) { this.reservaActual = reservaActual; }
}