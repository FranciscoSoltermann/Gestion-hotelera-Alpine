package org.TPDesarrollo.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.TPDesarrollo.enums.RazonSocial;
import org.TPDesarrollo.enums.TipoDocumento;
import java.time.LocalDate;

/**
 * DTO para representar la información de un huésped.
 * Incluye validaciones para asegurar la integridad de los datos.
 * Contiene atributos como nombre, apellido, teléfono, tipo y número de documento,
 * fecha de nacimiento, nacionalidad, email, dirección, CUIT, ocupación y posición
 * frente al IVA.
 * Utiliza anotaciones de validación de Jakarta para garantizar que los datos
 * cumplan con los requisitos especificados.
 */
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
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "La Nacionalidad no puede estar vacío")
    @Size(min = 2, max = 50, message = "La Nacionalidad debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "La Nacionalidad solo puede contener letras y espacios")
    private String nacionalidad;

    @Email(message = "El formato del email no es válido")
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

    @NotNull(message = "La posición frente al IVA es obligatoria")
    private RazonSocial posicionIVA;

    @AssertTrue(message = "El CUIT es obligatorio si la posición frente al IVA no es 'Consumidor Final'.")
    private boolean isCuitConsistente() {

        // Si posicionIVA es nula
        // (Dejamos que el @NotBlank de posicionIVA maneje ese error)
        if (posicionIVA == null) {
            return true;
        }

        //Si es Consumidor Final, la validación pasa.
        // No nos importa si el CUIT es nulo o no.
        if (posicionIVA.name().equalsIgnoreCase("Consumidor_Final") || posicionIVA.toString().equalsIgnoreCase("Consumidor Final")) {
            return true;
        }

        // Si NO es Consumidor Final (es "Responsable Inscripto", "Monotributista", etc.),
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
    public RazonSocial getPosicionIVA() { return posicionIVA; }
    public void setPosicionIVA(RazonSocial posicionIVA) { this.posicionIVA = posicionIVA; }
}
