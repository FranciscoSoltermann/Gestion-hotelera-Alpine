package org.TPDesarrollo.clases;

import jakarta.persistence.*;

@Entity
@Table(name = "ocupante", schema = "pruebabdd")
@PrimaryKeyJoinColumn(name = "id_persona")
public class Ocupante extends Persona {

    @ManyToOne
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    // ✅ NUEVO: Agregamos esto para conectar con la tabla Habitacion
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

    // --- GETTERS Y SETTERS ---

    public Reserva getReserva() { return reserva; }
    public void setReserva(Reserva reserva) { this.reserva = reserva; }

    // ✅ NUEVO: Getters y Setters para Habitacion
    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }

    // --- MÉTODOS PUENTE ---

    public String getDni() {
        return this.getDocumento();
    }

    public void setDni(String dni) {
        this.setDocumento(dni);
    }
}