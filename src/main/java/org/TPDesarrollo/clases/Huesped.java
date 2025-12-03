package org.TPDesarrollo.clases;

import jakarta.persistence.*;
import org.TPDesarrollo.enums.RazonSocial;

import java.time.LocalDate;

/**
 * Clase Huesped que extiende de Persona.
 * Representa a un huésped con atributos específicos.
 * Utiliza el patrón Builder para la creación de instancias.
 * Hereda los atributos comunes de la clase Persona.
 * Está mapeada a la tabla "huesped" en la base de datos.
 * Contiene atributos como cuit, ocupación, nacionalidad, fecha de nacimiento,
 * posición frente al IVA, email y una relación con la clase Direccion.
 * Utiliza anotaciones JPA para el mapeo de la base de datos.
 * Implementa un constructor privado que recibe un builder para facilitar la creación de objetos.
 * Proporciona getters y setters para todos los atributos.
 * Incluye una clase estática interna HuespedBuilder que extiende de PersonaBuilder
 * para construir objetos Huesped de manera fluida.
 * La relación con Direccion es ManyToOne con carga EAGER y cascada ALL.
 * El atributo cuit es único en la base de datos.
 * La fecha de nacimiento se mapea a la columna "fecha_nac".
 * La posición frente al IVA se mapea a la columna "pos_iva" como un enumerado.
 */
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

    // Usamos EAGER para que la dirección venga cargada sí o sí.
    // Esto evita el error silencioso al convertir a JSON.
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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
        this.direccion = builder.direccion;
    }

    public static HuespedBuilder builder() {
        return new HuespedBuilder();
    }

    //GETTERS / SETTERS
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

    /**
     * Builder estático para la clase Huesped.
     * Extiende de PersonaBuilder para heredar los atributos comunes.
     * Proporciona métodos para establecer los atributos específicos de Huesped.
     * Implementa el método build() para crear una instancia de Huesped.
     */
    public static class HuespedBuilder extends PersonaBuilder<Huesped, HuespedBuilder> {

        private String cuit;
        private String ocupacion;
        private String nacionalidad;
        private LocalDate fechaNacimiento;
        private RazonSocial posicionIVA;
        private String email;
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