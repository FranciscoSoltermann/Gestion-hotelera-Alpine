package org.TPDesarrollo.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

public class ReservaDTO {

    private Integer id;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    @FutureOrPresent (message = "La fecha de Ingreso debe ser a partir del dia de hoy.")
    private LocalDate ingreso;

    @NotNull(message = "La fecha de egreso es obligatoria")
    @FutureOrPresent (message = "La fecha de Egreso debe ser a partir del dia de hoy.")
    private LocalDate egreso;

    // ELIMINADO: private Integer idPersona; (El frontend manda un objeto, no un ID suelto)

    @NotNull(message = "Los datos del huésped son obligatorios")
    private DatosHuespedReserva huesped; // Usamos una clase interna personalizada

    @NotEmpty(message = "Debe seleccionar al menos una habitación")
    private List<Integer> habitaciones;

    public ReservaDTO() {}

    // --- CLASE INTERNA PARA EVITAR VALIDACIONES ESTRICTAS DE HUESPEDDTO ---
    public static class DatosHuespedReserva {
        public String tipoDocumento;
        public String documento;
        public String nombre;
        public String apellido;
        public String telefono;

        // Getters y Setters
        public String getTipoDocumento() { return tipoDocumento; }
        public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
        public String getDocumento() { return documento; }
        public void setDocumento(String documento) { this.documento = documento; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getApellido() { return apellido; }
        public void setApellido(String apellido) { this.apellido = apellido; }
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
    }

    // --- GETTERS Y SETTERS PRINCIPALES ---
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
}