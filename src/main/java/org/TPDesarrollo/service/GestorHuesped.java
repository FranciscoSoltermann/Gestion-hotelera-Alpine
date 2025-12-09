package org.TPDesarrollo.service;

import org.TPDesarrollo.entity.Huesped;
import org.TPDesarrollo.dto.HuespedDTO;
import java.util.List;
/**
 * Interfaz para la gestión de huéspedes.
 */
public interface GestorHuesped {
    /**
     * Busca un huésped por su DNI.
     *
     * @param dni El DNI del huésped a buscar.
     * @return Un objeto HuespedDTO que representa al huésped encontrado.
     */
    HuespedDTO buscarPorDni(String dni);
    /**
     * Busca huéspedes según los criterios proporcionados.
     *
     * @param apellido       El apellido del huésped.
     * @param nombre         El nombre del huésped.
     * @param tipoDocumento  El tipo de documento del huésped.
     * @param documento      El número de documento del huésped.
     * @return Una lista de objetos HuespedDTO que cumplen con los criterios de búsqueda.
     */
    List<HuespedDTO> buscarHuespedes(String apellido, String nombre, String tipoDocumento, String documento);
    /**
     * Obtiene un huésped seleccionado por su ID.
     *
     * @param idHuesped El ID del huésped a obtener.
     * @return Un objeto HuespedDTO que representa al huésped seleccionado.
     */
    HuespedDTO obtenerHuespedSeleccionado(Integer idHuesped);
    /**
     * Da de alta un nuevo huésped.
     *
     * @param dto El objeto HuespedDTO que contiene los datos del huésped a dar de alta.
     * @return El objeto Huesped que ha sido dado de alta.
     */
    Huesped darDeAltaHuesped(HuespedDTO dto);
    /**
     * Modifica los datos de un huésped existente.
     *
     * @param dto El objeto HuespedDTO que contiene los datos actualizados del huésped.
     */
    void modificarHuesped(HuespedDTO dto);
    /**
     * Elimina un huésped por su ID.
     *
     * @param id El ID del huésped a eliminar.
     */
    void eliminarHuesped(Integer id);
}