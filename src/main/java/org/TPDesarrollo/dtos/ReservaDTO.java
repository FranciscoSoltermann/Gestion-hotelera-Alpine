package org.TPDesarrollo.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DTO para manejar reservas de habitaciones.
 * Incluye datos del huésped titular, fechas y habitaciones.
 * Además, permite especificar ocupantes por habitación.
 * Se utiliza para transferir datos entre capas de la aplicación.
 * Contiene validaciones para asegurar la integridad de los datos.
 */
public class ReservaDTO {

    private Integer id;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    private LocalDate ingreso;

    @NotNull(message = "La fecha de egreso es obligatoria")
    private LocalDate egreso;

    @NotNull(message = "Los datos del titular son obligatorios")
    @Valid
    private DatosHuespedReserva huesped;

    @NotEmpty(message = "Debe seleccionar al menos una habitación")
    private List<Integer> habitaciones;



    private Map<Integer, List<OcupanteDTO>> ocupantesPorHabitacion;

    public ReservaDTO() {}

    public static class DatosHuespedReserva {
        @NotBlank(message = "Tipo Doc obligatorio") public String tipoDocumento;
        @NotBlank(message = "Documento obligatorio") public String documento;
        @NotBlank(message = "Nombre obligatorio") public String nombre;
        @NotBlank(message = "Apellido obligatorio") public String apellido;
        public String telefono;

        // Getters/Setters
        public String getTipoDocumento() { return tipoDocumento; }
        public void setTipoDocumento(String t) { this.tipoDocumento = t; }
        public String getDocumento() { return documento; }
        public void setDocumento(String d) { this.documento = d; }
        public String getNombre() { return nombre; }
        public void setNombre(String n) { this.nombre = n; }
        public String getApellido() { return apellido; }
        public void setApellido(String a) { this.apellido = a; }
        public String getTelefono() { return telefono; }
        public void setTelefono(String t) { this.telefono = t; }
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDate getIngreso() { return ingreso; }
    public void setIngreso(LocalDate ingreso) { this.ingreso = ingreso; }
    public LocalDate getEgreso() { return egreso; }
    public void setEgreso(LocalDate egreso) { this.egreso = egreso; }
    public DatosHuespedReserva getHuesped() { return huesped; }
    public void setHuesped(DatosHuespedReserva huesped) { this.huesped = huesped; }
    public List<Integer> getHabitaciones() { return habitaciones; }
    public void setHabitaciones(List<Integer> habitaciones) { this.habitaciones = habitaciones; }

    public Map<Integer, List<OcupanteDTO>> getOcupantesPorHabitacion() { return ocupantesPorHabitacion; }
    public void setOcupantesPorHabitacion(Map<Integer, List<OcupanteDTO>> ocupantesPorHabitacion) { this.ocupantesPorHabitacion = ocupantesPorHabitacion; }
}