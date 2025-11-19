package org.TPDesarrollo.gestores;

import org.TPDesarrollo.repository.HuespedRepository;
import org.TPDesarrollo.exceptions.DniExistente;
import org.TPDesarrollo.exceptions.CuitExistente;
import org.TPDesarrollo.clases.Direccion;
import org.TPDesarrollo.clases.Huesped;
import org.TPDesarrollo.dtos.DireccionDTO;
import org.TPDesarrollo.dtos.HuespedDTO;
import org.TPDesarrollo.enums.TipoDocumento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GestorHuesped {

    // 1. CAMBIO: Inyección por Constructor (Variable final)
    private final HuespedRepository huespedRepository;

    @Autowired
    public GestorHuesped(HuespedRepository huespedRepository) {
        this.huespedRepository = huespedRepository;
    }

    @Transactional(readOnly = true)
    public List<HuespedDTO> buscarHuespedes(String apellido, String nombre, String tipoDocumento, String documento) {

        System.out.println("GESTOR: Solicitud para buscar huéspedes...");

        // Manejo robusto de parámetros (Igual que antes)
        String apellidoParam = (apellido != null && !apellido.trim().isEmpty()) ? apellido.trim() : null;
        String nombreParam = (nombre != null && !nombre.trim().isEmpty()) ? nombre.trim() : null;
        String documentoParam = (documento != null && !documento.trim().isEmpty()) ? documento.trim() : null;

        TipoDocumento tipoDocEnum = null;
        if (tipoDocumento != null && !tipoDocumento.trim().isEmpty()) {
            try {
                tipoDocEnum = TipoDocumento.valueOf(tipoDocumento.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Tipo documento inválido, se ignorará.");
            }
        }

        List<Huesped> huespedesEncontrados = huespedRepository.buscarHuespedesPorCriterios(
                apellidoParam, nombreParam, tipoDocEnum, documentoParam
        );

        return huespedesEncontrados.stream()
                .map(this::convertirA_DTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HuespedDTO obtenerHuespedSeleccionado(Integer idHuesped) {
        return huespedRepository.findById(idHuesped)
                .map(this::convertirA_DTO)
                .orElse(null);
    }

    @Transactional
    public Huesped darDeAltaHuesped(HuespedDTO huespedDTO) {
        System.out.println("GESTOR: Solicitud para dar de alta a " + huespedDTO.getNombre());

        // Validaciones
        if (huespedRepository.existsByDocumento(huespedDTO.getDocumento())) {
            throw new DniExistente("El DNI/Documento ingresado ya existe.");
        }

        String cuit = huespedDTO.getCuit();
        if (cuit != null && !cuit.trim().isEmpty()) {
            if (huespedRepository.existsByCuit(cuit)) {
                throw new CuitExistente("El CUIT ya existe: " + cuit);
            }
        } else {
            huespedDTO.setPosicionIVA("Consumidor Final");
        }

        // Conversión usando el nuevo método con BUILDER
        Huesped huespedEntidad = convertirA_Entidad(huespedDTO);

        return huespedRepository.save(huespedEntidad);
    }

    @Transactional
    public void modificarHuesped(HuespedDTO huespedDTO) {
        // OJO: Para modificar, lo ideal es buscar primero, actualizar campos y guardar.
        // Pero si usas convertirA_Entidad
        Huesped huespedEntidad = convertirA_Entidad(huespedDTO);
        huespedRepository.save(huespedEntidad);
    }

    @Transactional
    public void darDeBajaHuesped(Integer id) {
        huespedRepository.deleteById(id);
    }

    // --- MÉTODOS DE CONVERSIÓN REFACTORIZADOS CON BUILDER ---

    private HuespedDTO convertirA_DTO(Huesped entidad) {
        // Los DTO suelen ser simples, el constructor vacío + setters está bien,
        // o podrías hacer un Builder para el DTO también si quisieras.
        HuespedDTO dto = new HuespedDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setApellido(entidad.getApellido());
        dto.setTelefono(entidad.getTelefono());
        dto.setTipoDocumento(entidad.getTipoDocumento());
        dto.setDocumento(entidad.getDocumento());

        if (entidad.getDireccion() != null) {
            dto.setDireccion(new DireccionDTO(entidad.getDireccion()));
        }

        dto.setCuit(entidad.getCuit());
        dto.setEmail(entidad.getEmail());
        dto.setFechaNacimiento(entidad.getFechaNacimiento());
        dto.setNacionalidad(entidad.getNacionalidad());
        dto.setOcupacion(entidad.getOcupacion());
        // Si usas Enum en Huesped, conviértelo a String aquí:
        // dto.setPosicionIVA(entidad.getPosicionIVA().toString());
        dto.setPosicionIVA(entidad.getPosicionIVA());

        return dto;
    }

    private Huesped convertirA_Entidad(HuespedDTO dto) {
        // 2. CAMBIO: Uso de Builder para Dirección
        Direccion direccionEntidad = null;
        if (dto.getDireccion() != null) {
            DireccionDTO dDto = dto.getDireccion();

            direccionEntidad = Direccion.builder()
                    .calle(dDto.getCalle())
                    .numero(dDto.getNumero())
                    .pais(dDto.getPais())
                    .provincia(dDto.getProvincia())
                    .localidad(dDto.getLocalidad())
                    .departamento(dDto.getDepartamento())
                    .piso(dDto.getPiso())
                    .codigoPostal(dDto.getCodigoPostal())
                    .build();
        }

        // 3. CAMBIO: Uso de Builder para Huesped (con herencia de Persona)
        // ¡Mira qué limpio queda esto comparado con los setters!

        Huesped huesped = Huesped.builder()
                // Datos de Persona
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .telefono(dto.getTelefono())
                .documento(dto.getDocumento())
                .tipoDocumento(dto.getTipoDocumento())
                .direccion(direccionEntidad)
                // Datos de Huesped
                .cuit(dto.getCuit())
                .email(dto.getEmail())
                .nacionalidad(dto.getNacionalidad())
                .ocupacion(dto.getOcupacion())
                .fechaNacimiento(dto.getFechaNacimiento())
                .posicionIVA(dto.getPosicionIVA()) // Si cambiaste a Enum: RazonSocial.valueOf(dto.getPosicionIVA())
                .build();

        // Si es una actualización, seteamos el ID manualmente (el Builder crea objetos nuevos)
        if (dto.getId() != null) {
            huesped.setId(dto.getId());
        }

        return huesped;
    }

    // El método 'convertirDireccionA_Entidad' ya no es necesario porque
    // lo integramos arriba con el Builder, queda mucho más directo.
}