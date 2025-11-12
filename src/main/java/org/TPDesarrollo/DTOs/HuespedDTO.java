package org.TPDesarrollo.DTOs;

import jakarta.validation.Valid;
import org.TPDesarrollo.Enums.TipoDocumento;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.AssertTrue;

public class HuespedDTO {
    private Integer id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "El apellido solo puede contener letras y espacios")
    private String apellido;

    @NotBlank(message = "El Telefono no puede estar vacío")
    @Pattern(regexp = "^[0-9]{7,20}$", message = "El Telefono debe contener solo números")
    private String telefono;

    private TipoDocumento tipoDocumento;

    @NotBlank(message = "El DNI/Documento no puede estar vacío")
    // Esta regex ^[0-9]{7,8}$ significa:
    // ^ = Empieza la cadena
    // [0-9] = Solo se permiten dígitos del 0 al 9
    // {7,8} = Debe tener una longitud de 7 u 8 dígitos
    // $ = Termina la cadena
    @Pattern(regexp = "^[0-9]{7,8}$", message = "El DNI debe contener solo 7 u 8 números, sin letras ni puntos.")
    private String documento;
    // ahora String
    private LocalDate fechaNacimiento;

    @NotBlank(message = "La Nacionalidad no puede estar vacío")
    @Size(min = 2, max = 50, message = "La Nacionalidad debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "La Nacionalidad solo puede contener letras y espacios")
    private String nacionalidad;

    private String email;
    @Valid
    private DireccionDTO direccion;


    @Pattern(regexp = "^[0-9]{11}$", message = "El CUIT debe contener exactamente 11 números, sin letras ni guiones.")
    private String cuit;


    @NotBlank(message = "La ocupación не puede estar vacía")
    // Esta regex es similar a la de nombre/apellido
    // ^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+$ = Solo letras (con acentos, ñ) y espacios.
    // El '+' al final significa "uno o más caracteres"
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "La ocupación solo puede contener letras y espacios.")
    private String ocupacion;

    @NotBlank(message = "La posición frente al IVA es obligatoria")
    private String posicionIVA;

    @AssertTrue(message = "El CUIT es obligatorio si la posición frente al IVA no es 'Consumidor Final'.")
    private boolean isCuitConsistente() {

        // Si posicionIVA es nula, no podemos validar.
        // (Dejamos que el @NotBlank de posicionIVA maneje ese error)
        if (posicionIVA == null) {
            return true;
        }

        // CASO 1: Si es Consumidor Final, la validación pasa.
        // No nos importa si el CUIT es nulo o no.
        if (posicionIVA.equalsIgnoreCase("Consumidor Final")) {
            return true;
        }

        // CASO 2: Si NO es Consumidor Final (es "Responsable Inscripto", etc.),
        // el CUIT DEBE estar presente (no ser nulo ni estar vacío).
        return cuit != null && !cuit.isBlank();
    }

    public HuespedDTO() {}

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public TipoDocumento getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(TipoDocumento tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public DireccionDTO getDireccion() { return direccion; }
    public void setDireccion(DireccionDTO direccion) { this.direccion = direccion; }
    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }
    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }
    public String getPosicionIVA() { return posicionIVA; }
    public void setPosicionIVA(String posicionIVA) { this.posicionIVA = posicionIVA; }
}
