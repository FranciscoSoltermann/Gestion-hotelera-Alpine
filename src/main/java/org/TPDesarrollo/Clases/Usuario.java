package org.TPDesarrollo.Clases;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario", schema = "pruebabdd")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "contrasenia", nullable = false) // 👈 importante: coincide con la BD
    private String contrasenia;

    @Column(name = "rol")
    private String rol;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_huesped")
    private Huesped huesped;

    public Usuario() {}

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Huesped getHuesped() { return huesped; }
    public void setHuesped(Huesped huesped) { this.huesped = huesped; }
}
