package org.TPDesarrollo.clases;

import jakarta.persistence.*;

@Entity
@Table(name = "ocupante", schema = "pruebabdd")
@PrimaryKeyJoinColumn(name = "id_ocupante") // Vincula el ID de Ocupante con el ID de Persona
public class Ocupante extends Persona {

    // Ya NO definimos id, nombre, apellido, dni aquí porque se heredan de Persona.

    @ManyToOne
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    public Ocupante() {
        super();
    }

    public Ocupante(String nombre, String apellido, String dni, Reserva reserva) {
        super();
        this.setNombre(nombre);
        this.setApellido(apellido);
        // Asumo que Persona usa 'documento'. Si usa 'dni', cámbialo aquí.
        this.setDocumento(dni);
        this.reserva = reserva;
    }

    // --- GETTERS Y SETTERS PROPIOS ---

    public Reserva getReserva() { return reserva; }
    public void setReserva(Reserva reserva) { this.reserva = reserva; }

    // --- MÉTODOS PUENTE (Para compatibilidad con tu código actual) ---
    // Si tu DTO y Gestor usan "getDni/setDni" pero Persona usa "getDocumento",
    // estos métodos hacen la traducción automática.

    public String getDni() {
        return this.getDocumento();
    }

    public void setDni(String dni) {
        this.setDocumento(dni);
    }
}