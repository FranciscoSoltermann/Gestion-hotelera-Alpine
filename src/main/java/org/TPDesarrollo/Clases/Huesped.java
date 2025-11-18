package org.TPDesarrollo.Clases;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "huesped")
public class Huesped extends Persona {

    @Column(unique = true)
    private String cuit;
    private String ocupacion;
    private String nacionalidad;
    @Column(name = "fecha_nac")
    private LocalDate fechaNacimiento;
    @Column(name = "pos_iva")
    private String posicionIVA;
    private String email;

    // 1. Constructor vacío para JPA
    public Huesped() {}

    // 2. Constructor privado que usa el Builder
    private Huesped(HuespedBuilder builder) {
        super(builder); // <--- ¡LA MAGIA! Persona se encarga de sus propios campos
        this.cuit = builder.cuit;
        this.ocupacion = builder.ocupacion;
        this.nacionalidad = builder.nacionalidad;
        this.fechaNacimiento = builder.fechaNacimiento;
        this.posicionIVA = builder.posicionIVA;
        this.email = builder.email;
    }

    public static HuespedBuilder builder() {
        return new HuespedBuilder();
    }

    // Getters y Setters (Sin cambios)
    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }
    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getPosicionIVA() { return posicionIVA; }
    public void setPosicionIVA(String posicionIVA) { this.posicionIVA = posicionIVA; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // --- CLASE BUILDER ---
    // Hereda de PersonaBuilder y se pasa a sí mismo como tipo genérico
    public static class HuespedBuilder extends PersonaBuilder<Huesped, HuespedBuilder> {

        // SOLO declaramos los campos exclusivos de Huesped
        private String cuit;
        private String ocupacion;
        private String nacionalidad;
        private LocalDate fechaNacimiento;
        private String posicionIVA;
        private String email;

        public HuespedBuilder() {}

        // Implementación del método abstracto del padre para mantener el encadenamiento
        @Override
        protected HuespedBuilder self() {
            return this;
        }

        // Implementación del método build()
        @Override
        public Huesped build() {
            return new Huesped(this);
        }

        // Métodos fluidos SOLO para Huesped (los de Persona ya vienen heredados)
        public HuespedBuilder cuit(String cuit) {
            this.cuit = cuit;
            return this;
        }

        public HuespedBuilder ocupacion(String ocupacion) {
            this.ocupacion = ocupacion;
            return this;
        }

        public HuespedBuilder nacionalidad(String nacionalidad) {
            this.nacionalidad = nacionalidad;
            return this;
        }

        public HuespedBuilder fechaNacimiento(LocalDate fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
            return this;
        }

        public HuespedBuilder posicionIVA(String posicionIVA) {
            this.posicionIVA = posicionIVA;
            return this;
        }

        public HuespedBuilder email(String email) {
            this.email = email;
            return this;
        }
    }
}