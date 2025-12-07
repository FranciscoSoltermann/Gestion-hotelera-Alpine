package org.TPDesarrollo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsableDePagoDTO {
    private Integer id;

    // --- CAMPOS PARA LA LISTA ---
    private String nombreCompleto;
    private String documento;
    private String tipo;           // "FISICA" o "JURIDICA"

    // --- CAMPOS PARA EL ALTA DE EMPRESA ---
    private String razonSocial;
    private String cuit;
    private String telefono;

    // --- DATOS DE DIRECCIÓN ---
    private String calle;
    private String numero;
    private String departamento;
    private String piso;
    private String codigoPostal;
    private String localidad;
    private String provincia;
    private String pais;
}