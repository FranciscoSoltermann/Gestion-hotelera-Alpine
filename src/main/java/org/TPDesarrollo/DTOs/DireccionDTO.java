package org.TPDesarrollo.DTOs;

import org.TPDesarrollo.Clases.Direccion;

public class DireccionDTO {
    private String pais;
    private String provincia;
    private String localidad;
    private String calle;
    private String numero; // <-- String para coincidir con la entidad
    private String departamento;
    private String piso;
    private String codigoPostal;

    public DireccionDTO() {}

    public DireccionDTO(String pais, String provincia, String localidad, String calle, String numero, String departamento, String piso, String codigoPostal) {
        this.pais = pais;
        this.provincia = provincia;
        this.localidad = localidad;
        this.calle = calle;
        this.numero = numero;
        this.departamento = departamento;
        this.piso = piso;
        this.codigoPostal = codigoPostal;
    }

    public DireccionDTO(Direccion direccion) {
        if (direccion == null) return;
        this.pais = direccion.getPais();
        this.provincia = direccion.getProvincia();
        this.localidad = direccion.getLocalidad();
        this.calle = direccion.getCalle();
        this.numero = direccion.getNumero();
        this.departamento = direccion.getDepartamento();
        this.piso = direccion.getPiso();
        this.codigoPostal = direccion.getCodigoPostal();
    }

    // Getters y Setters (void)
    public String getNumero() { return numero; }
    public String getPais() { return pais; }
    public String getProvincia() { return provincia; }
    public String getLocalidad() { return localidad; }
    public String getCalle() { return calle; }
    public String getDepartamento() { return departamento; }
    public String getPiso() { return piso; }
    public String getCodigoPostal() { return codigoPostal; }

    public void setNumero(String numero) { this.numero = numero; }
    public void setPais(String pais) { this.pais = pais; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }
    public void setCalle(String calle) { this.calle = calle; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public void setPiso(String piso) { this.piso = piso; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
}
