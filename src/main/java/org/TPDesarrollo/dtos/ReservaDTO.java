package org.TPDesarrollo.dtos;

import org.TPDesarrollo.enums.TipoDocumento;
import java.time.LocalDate;
import java.util.List;

public class ReservaDTO {
    // Datos de la reserva
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<Long> idHabitaciones; // Lista de IDs de las habitaciones seleccionadas

    // Datos del Huésped (Requeridos por el diagrama para buscar o crear)
    private String nombre;
    private String apellido;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private String telefono;
    private String email; // Opcional si lo usas

    // Genera los Getters y Setters aquí
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public List<Long> getIdHabitaciones() { return idHabitaciones; }
    public void setIdHabitaciones(List<Long> idHabitaciones) { this.idHabitaciones = idHabitaciones; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public TipoDocumento getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(TipoDocumento tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return email; }
}