package org.TPDesarrollo.clases;

import jakarta.persistence.*;
import org.TPDesarrollo.enums.TipoDocumento;

// @Entity <-- Si usas estrategia JOINED, usa @Entity y @Inheritance
// @Inheritance(strategy = InheritanceType.JOINED)
@MappedSuperclass // <-- Si usas esto, no habrá tabla 'persona', sus campos irán a los hijos.
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
    private TipoDocumento tipoDocumento;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;

    // 1. Constructor vacío (Obligatorio para JPA)
    public Persona() {}

    // 2. Constructor protegido para que los hijos (Huesped/Usuario) lo usen
    protected Persona(PersonaBuilder<?, ?> builder) {
        this.nombre = builder.nombre;
        this.apellido = builder.apellido;
        this.telefono = builder.telefono;
        this.documento = builder.documento;
        this.tipoDocumento = builder.tipoDocumento;
        this.direccion = builder.direccion;
    }

    // Getters y Setters (Necesarios para JPA y lógica)
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
    public TipoDocumento getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(TipoDocumento tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }

    // --- PATRÓN BUILDER GENÉRICO ---

    // T: El tipo de la clase concreta (ej. Huesped)
    // B: El tipo del Builder concreto (ej. HuespedBuilder)
    public static abstract class PersonaBuilder<T extends Persona, B extends PersonaBuilder<T, B>> {
        protected String nombre;
        protected String apellido;
        protected String telefono;
        protected String documento;
        protected TipoDocumento tipoDocumento;
        protected Direccion direccion;

        // Método abstracto para que el hijo se devuelva a sí mismo ('this')
        protected abstract B self();

        public abstract T build();

        public B nombre(String nombre) {
            this.nombre = nombre;
            return self();
        }

        public B apellido(String apellido) {
            this.apellido = apellido;
            return self();
        }

        public B telefono(String telefono) {
            this.telefono = telefono;
            return self();
        }

        public B documento(String documento) {
            this.documento = documento;
            return self();
        }

        public B tipoDocumento(TipoDocumento tipoDocumento) {
            this.tipoDocumento = tipoDocumento;
            return self();
        }

        public B direccion(Direccion direccion) {
            this.direccion = direccion;
            return self();
        }
    }
}