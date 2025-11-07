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

    // Relacion inversa optional al Usuario
    @OneToOne(mappedBy = "huesped", fetch = FetchType.LAZY)
    private Usuario usuario;

    public Huesped() {}

    // Getters / Setters
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

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
