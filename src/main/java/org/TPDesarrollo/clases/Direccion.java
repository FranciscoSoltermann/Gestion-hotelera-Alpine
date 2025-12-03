package org.TPDesarrollo.clases;

import jakarta.persistence.*;

/**
 * Clase que representa una dirección física.
 * Implementa el patrón Builder para facilitar la creación de instancias.
 * Utiliza anotaciones JPA para el mapeo a una tabla de base de datos.
 */
@Entity
@Table(name = "direccion")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Integer id;

    private String localidad;
    private String piso;
    private String departamento;
    private String numero;
    private String calle;

    @Column(name = "cod_postal")
    private String codigoPostal;

    private String pais;
    private String provincia;

    /**
     * --- PATRÓN BUILDER IMPLEMENTADO ---
     * Constructor vacío requerido por JPA
     * Constructor privado que recibe el Builder
     * Método estático para iniciar el Builder
     * Clase interna estática Builder con métodos fluidos
     * Método build() para construir la instancia final
     */
    // Constructor vacío requerido por JPA
    public Direccion() {}

    // Constructor privado para que solo el Builder pueda usarlo
    private Direccion(DireccionBuilder builder) {
        this.pais = builder.pais;
        this.provincia = builder.provincia;
        this.localidad = builder.localidad;
        this.calle = builder.calle;
        this.numero = builder.numero;
        this.departamento = builder.departamento;
        this.piso = builder.piso;
        this.codigoPostal = builder.codigoPostal;
    }

    // Método estático para iniciar el Builder
    public static DireccionBuilder builder() {
        return new DireccionBuilder();
    }

    // Getters y Setters (necesarios para JPA)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }
    public String getPiso() { return piso; }
    public void setPiso(String piso) { this.piso = piso; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }
    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    //CLASE INTERNA ESTATICA BUILDER
    public static class DireccionBuilder {
        private String pais;
        private String provincia;
        private String localidad;
        private String calle;
        private String numero;
        private String departamento;
        private String piso;
        private String codigoPostal;


        // Constructor del builder
        public DireccionBuilder() {}

        // Métodos fluidos (retornan 'this')
        public DireccionBuilder pais(String pais) {
            this.pais = pais;
            return this;
        }

        public DireccionBuilder provincia(String provincia) {
            this.provincia = provincia;
            return this;
        }

        public DireccionBuilder localidad(String localidad) {
            this.localidad = localidad;
            return this;
        }

        public DireccionBuilder calle(String calle) {
            this.calle = calle;
            return this;
        }

        public DireccionBuilder numero(String numero) {
            this.numero = numero;
            return this;
        }

        public DireccionBuilder departamento(String departamento) {
            this.departamento = departamento;
            return this;
        }

        public DireccionBuilder piso(String piso) {
            this.piso = piso;
            return this;
        }

        public DireccionBuilder codigoPostal(String codigoPostal) {
            this.codigoPostal = codigoPostal;
            return this;
        }

        // Método final para construir el objeto
        public Direccion build() {
            return new Direccion(this);
        }
    }
}