package org.TPDesarrollo.Gestores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.TPDesarrollo.Clases.Direccion;
import org.TPDesarrollo.Clases.Huesped;
import org.TPDesarrollo.DAOS.HuespedDAO;
import org.TPDesarrollo.DTOs.DireccionDTO;
import org.TPDesarrollo.DTOs.HuespedDTO;
import org.TPDesarrollo.Excepciones.CuitExistente;
import org.TPDesarrollo.Enums.TipoDocumento;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GestorHuesped {

    @Autowired
    private HuespedDAO huespedDAO;

    @Transactional(readOnly = true)
    public List<HuespedDTO> buscarHuespedes(String apellido, String nombre, String tipoDocumento, String documento) {
        System.out.println("GESTOR: Solicitud para buscar huéspedes...");

        TipoDocumento tipoDocEnum = null;
        if (tipoDocumento != null && !tipoDocumento.isBlank()) {
            try {
                tipoDocEnum = TipoDocumento.valueOf(tipoDocumento.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                tipoDocEnum = null;
            }
        }

        List<Huesped> huespedesEncontrados = huespedDAO.buscarHuespedesPorCriterios(
                (apellido == null || apellido.isEmpty()) ? null : apellido,
                (nombre == null || nombre.isEmpty()) ? null : nombre,
                tipoDocEnum,
                (documento == null || documento.isEmpty()) ? null : documento
        );

        return huespedesEncontrados.stream()
                .map(this::convertirA_DTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HuespedDTO obtenerHuespedSeleccionado(Integer idHuesped) {
        return huespedDAO.findById(idHuesped)
                .map(this::convertirA_DTO)
                .orElse(null);
    }

    @Transactional
    public void darDeAltaHuesped(HuespedDTO huespedDTO) {
        System.out.println("GESTOR: Solicitud para dar de alta a " + huespedDTO.getNombre());
        String cuit = huespedDTO.getCuit();

        if (cuit != null && !cuit.trim().isEmpty()) {
            if (huespedDAO.existsByCuit(cuit)) {
                throw new CuitExistente(cuit);
            }
        } else {
            huespedDTO.setPosicionIVA("Consumidor Final");
        }

        Huesped huespedEntidad = convertirA_Entidad(huespedDTO);
        huespedDAO.save(huespedEntidad);
    }

    @Transactional
    public void modificarHuesped(HuespedDTO huespedDTO) {
        System.out.println("GESTOR: Solicitud para modificar al huésped ID: " + huespedDTO.getId());
        Huesped huespedEntidad = convertirA_Entidad(huespedDTO);
        huespedDAO.save(huespedEntidad);
        String nombre = (huespedEntidad.getNombre() != null) ? huespedEntidad.getNombre() : "N/A";
        System.out.println("GESTOR: Modificación de " + nombre + " exitosa.");
    }

    @Transactional
    public void darDeBajaHuesped(Integer id) {
        System.out.println("GESTOR: Solicitud para dar de baja al ID " + id);
        huespedDAO.deleteById(id);
    }

    private HuespedDTO convertirA_DTO(Huesped entidad) {
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
        dto.setPosicionIVA(entidad.getPosicionIVA());
        return dto;
    }

    private Huesped convertirA_Entidad(HuespedDTO dto) {
        Huesped entidad = new Huesped();

        if (dto.getId() != null) {
            entidad.setId(dto.getId());
        }

        // Seteamos los campos heredados de Persona directamente en la entidad Huesped
        entidad.setNombre(dto.getNombre());
        entidad.setApellido(dto.getApellido());
        entidad.setTelefono(dto.getTelefono());
        entidad.setDocumento(dto.getDocumento());
        entidad.setTipoDocumento(dto.getTipoDocumento());

        if (dto.getDireccion() != null) {
            entidad.setDireccion(convertirDireccionA_Entidad(dto.getDireccion()));
        }

        entidad.setCuit(dto.getCuit());
        entidad.setOcupacion(dto.getOcupacion());
        entidad.setPosicionIVA(dto.getPosicionIVA());
        entidad.setEmail(dto.getEmail());
        entidad.setFechaNacimiento(dto.getFechaNacimiento());
        entidad.setNacionalidad(dto.getNacionalidad());

        return entidad;
    }

    private Direccion convertirDireccionA_Entidad(DireccionDTO dto) {
        if (dto == null) return null;
        return new Direccion(
                dto.getPais(),
                dto.getProvincia(),
                dto.getLocalidad(),
                dto.getCalle(),
                dto.getNumero(),
                dto.getDepartamento(),
                dto.getPiso(),
                dto.getCodigoPostal()
        );
    }
}
