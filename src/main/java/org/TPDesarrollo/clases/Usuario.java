package org.TPDesarrollo.clases;

import jakarta.persistence.*;

/**
 * Clase que representa un usuario en el sistema.
 * Mapeada a la tabla "usuario" en el esquema "pruebabdd".
 * Implementa el patrón Builder para facilitar la creación de instancias.
 * Contiene atributos como id, nombre, contrasenia y rol.
 * Incluye un constructor vacío obligatorio para JPA, un constructor privado para el Builder,
 * y un método estático para acceder al Builder.
 * Además, proporciona getters y setters para todos los atributos.
 * La clase interna UsuarioBuilder permite la construcción fluida de objetos Usuario.
 * Cada método del Builder retorna la instancia del Builder para permitir encadenamiento.
 * El método build() crea y retorna una instancia de Usuario utilizando los valores establecidos en el Builder.
 */
@Entity
@Table(name = "usuario", schema = "pruebabdd")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "contrasenia", nullable = false)
    private String contrasenia;

    @Column(name = "rol")
    private String rol;

    //Constructor vacío obligatorio para JPA
    public Usuario() {}

    //Constructor privado para el Builder
    private Usuario(UsuarioBuilder builder) {
        this.nombre = builder.nombre;
        this.contrasenia = builder.contrasenia;
        this.rol = builder.rol;
        // El ID no se setea aquí porque lo genera la Base de Datos
    }

    //Método de acceso al Builder
    public static UsuarioBuilder builder() {
        return new UsuarioBuilder();
    }

    //Getters y Setters (Necesarios para JPA y Spring)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    //CLASE INTERNA BUILDER
    public static class UsuarioBuilder {
        private String nombre;
        private String contrasenia;
        private String rol;

        public UsuarioBuilder() {}

        public UsuarioBuilder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public UsuarioBuilder contrasenia(String contrasenia) {
            this.contrasenia = contrasenia;
            return this;
        }

        public UsuarioBuilder rol(String rol) {
            this.rol = rol;
            return this;
        }

        public Usuario build() {
            return new Usuario(this);
        }
    }
}