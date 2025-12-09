package org.TPDesarrollo.service;

import org.TPDesarrollo.dto.CargaConsumoDTO;
/**
 * Interfaz para la gestión de consumos.
 */
public interface GestorConsumo {
    /**
     * Carga un consumo basado en el DTO proporcionado.
     *
     * @param dto Objeto de transferencia de datos que contiene la información del consumo a cargar.
     */
    void cargarConsumo(CargaConsumoDTO dto);
}
