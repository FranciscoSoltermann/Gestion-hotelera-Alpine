package org.TPDesarrollo.dtos;

import org.TPDesarrollo.clases.Direccion;
// --- IMPORTA ESTAS LIBRERÍAS ---
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class DireccionDTO {

    @NotBlank(message = "El país es obligatorio")
    private String pais;

    @NotBlank(message = "La provincia es obligatoria")
    private String provincia;

    @NotBlank(message = "La localidad es obligatoria")
    private String localidad;

    @NotBlank(message = "La calle es obligatoria")
    private String calle;


    @NotBlank(message = "El número es obligatorio")
    @Pattern(regexp = "^[0-9]+$", message = "El número debe contener solo numeros.")
    private String numero;

    private String departamento;

    private String piso;

    // --- VALIDACIÓN PARA CÓDIGO POSTAL ---
    @NotBlank(message = "El código postal es obligatorio")
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
