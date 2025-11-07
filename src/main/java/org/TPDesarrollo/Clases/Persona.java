package org.TPDesarrollo.Clases;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String documento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_doc", nullable = false)
    private org.TPDesarrollo.Enums.TipoDocumento tipoDocumento;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;

    // Getters / Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public org.TPDesarrollo.Enums.TipoDocumento getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(org.TPDesarrollo.Enums.TipoDocumento tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }
}
