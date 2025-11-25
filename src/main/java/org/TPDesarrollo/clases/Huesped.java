package org.TPDesarrollo.clases;

import jakarta.persistence.*;
import org.TPDesarrollo.enums.RazonSocial;

import java.time.LocalDate;

@Entity
@Table(name = "huesped")
@PrimaryKeyJoinColumn(name = "id_persona")
public class Huesped extends Persona {

    @Column(unique = true)
    private String cuit;
    private String ocupacion;
    private String nacionalidad;

    @Column(name = "fecha_nac")
    private LocalDate fechaNacimiento;

    @Column(name = "pos_iva")
    @Enumerated(EnumType.STRING)
    private RazonSocial posicionIVA;

    private String email;

    // ⭐ ESTE ES EL CAMPO QUE FALTABA ⭐
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;

    public Huesped() {}

    private Huesped(HuespedBuilder builder) {
        super(builder);
        this.cuit = builder.cuit;
        this.ocupacion = builder.ocupacion;
        this.nacionalidad = builder.nacionalidad;
        this.fechaNacimiento = builder.fechaNacimiento;
        this.posicionIVA = builder.posicionIVA;
        this.email = builder.email;
        this.direccion = builder.direccion; // <--- ⭐ SE SETEA AQUÍ ⭐
    }

    public static HuespedBuilder builder() {
        return new HuespedBuilder();
    }

    // ---------- GETTERS / SETTERS ----------
    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }

    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public RazonSocial getPosicionIVA() { return posicionIVA; }
    public void setPosicionIVA(RazonSocial posicionIVA) { this.posicionIVA = posicionIVA; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }

    // ------------ BUILDER -------------
    public static class HuespedBuilder extends PersonaBuilder<Huesped, HuespedBuilder> {

        private String cuit;
        private String ocupacion;
        private String nacionalidad;
        private LocalDate fechaNacimiento;
        private RazonSocial posicionIVA;
        private String email;

        // ⭐ agregar campo direccion aquí ⭐
        private Direccion direccion;

        @Override
        protected HuespedBuilder self() {
            return this;
        }

        @Override
        public Huesped build() {
            return new Huesped(this);
        }

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

        public HuespedBuilder posicionIVA(RazonSocial posicionIVA) {
            this.posicionIVA = posicionIVA;
            return this;
        }

        public HuespedBuilder email(String email) {
            this.email = email;
            return this;
        }

        public HuespedBuilder direccion(Direccion direccion) {
            this.direccion = direccion;
            return this;
        }
    }
}
