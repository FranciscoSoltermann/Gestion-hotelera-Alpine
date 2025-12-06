package org.TPDesarrollo.service;

import org.TPDesarrollo.entity.Huesped;
import org.TPDesarrollo.dtos.HuespedDTO;
import java.util.List;

public interface GestorHuesped {

    HuespedDTO buscarPorDni(String dni);

    List<HuespedDTO> buscarHuespedes(String apellido, String nombre, String tipoDocumento, String documento);

    HuespedDTO obtenerHuespedSeleccionado(Integer idHuesped);

    Huesped darDeAltaHuesped(HuespedDTO dto);

    void modificarHuesped(HuespedDTO dto);

    void darDeBajaHuesped(Integer id);
}