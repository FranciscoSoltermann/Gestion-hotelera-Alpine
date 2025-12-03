package org.TPDesarrollo.clases;

import jakarta.persistence.*;

/**
 * Clase Ocupante
 * Representa a un ocupante asociado a una reserva y una habitación.
 * Hereda de la clase Persona.
 * Contiene una relación ManyToOne con Reserva y Habitacion.
 * Proporciona métodos para obtener y establecer la reserva y la habitación asociadas.
 * También incluye métodos puente para manejar el DNI.
 */
@Entity
@Table(name = "ocupante", schema = "pruebabdd")
@PrimaryKeyJoinColumn(name = "id_persona")
public class Ocupante extends Persona {

    @ManyToOne
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;


    @ManyToOne
    @JoinColumn(name = "id_habitacion")
    private Habitacion habitacion;


    public Ocupante() {
        super();
    }

    public Ocupante(String nombre, String apellido, String dni, Reserva reserva) {
        super();
        this.setNombre(nombre);
        this.setApellido(apellido);
        this.setDocumento(dni);
        this.reserva = reserva;
    }

    //Getters y setters

    public Reserva getReserva() { return reserva; }
    public void setReserva(Reserva reserva) { this.reserva = reserva; }

    //Getters y Setters para Habitacion
    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }

    //Metodos Puentes
    public String getDni() {
        return this.getDocumento();
    }

    public void setDni(String dni) {
        this.setDocumento(dni);
    }
}