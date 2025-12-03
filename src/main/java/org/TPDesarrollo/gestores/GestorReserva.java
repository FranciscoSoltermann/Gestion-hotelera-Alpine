package org.TPDesarrollo.gestores;

import org.TPDesarrollo.clases.*;
import org.TPDesarrollo.dtos.OcupanteDTO;
import org.TPDesarrollo.dtos.ReservaDTO;
import org.TPDesarrollo.enums.RazonSocial;
import org.TPDesarrollo.enums.TipoDocumento;
import org.TPDesarrollo.repository.HuespedRepository;
import org.TPDesarrollo.repository.HabitacionRepository;
import org.TPDesarrollo.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GestorReserva
 * Clase de servicio que maneja la lógica de negocio relacionada con reservas y ocupaciones de habitaciones.
 * Proporciona métodos para crear reservas y ocupaciones, validando disponibilidad y gestionando datos
 * de huéspedes y ocupantes.
 * Utiliza repositorios para interactuar con la base de datos.
 * Contiene lógica para validar fechas, verificar disponibilidad de habitaciones,
 * y gestionar la creación de reservas y ocupantes asociados.
 */
@Service
public class GestorReserva {

    private final HabitacionRepository habitacionRepository;
    private final ReservaRepository reservaRepository;
    private final HuespedRepository huespedRepository;
    // Constructor con inyección de dependencias
    public GestorReserva(HabitacionRepository hr, ReservaRepository rr, HuespedRepository phr) {
        this.habitacionRepository = hr;
        this.reservaRepository = rr;
        this.huespedRepository = phr;
    }

    //RESERVAR
    @Transactional
    public List<Reserva> crearReserva(ReservaDTO dto) {
        return procesarReserva(dto, "RESERVADA");
    }

    //OCUPAR (CHECK-IN)
    @Transactional
    public List<Reserva> crearOcupacion(ReservaDTO dto) {
        return procesarReserva(dto, "OCUPADA");
    }

    // Lógica común para reservar u ocupar
    private List<Reserva> procesarReserva(ReservaDTO dto, String estadoNuevo) {

        //Validar fechas
        if (dto.getIngreso().isAfter(dto.getEgreso())) {
            throw new IllegalArgumentException("La fecha de ingreso debe ser anterior al egreso");
        }

        //Obtener Habitaciones solicitadas
        List<Habitacion> habitaciones = habitacionRepository.findAllById(dto.getHabitaciones());
        if (habitaciones.size() != dto.getHabitaciones().size()) {
            throw new RuntimeException("Alguna habitación solicitada no existe");
        }

        //VALIDAR DISPONIBILIDAD
        for (Habitacion h : habitaciones) {
            List<Reserva> conflictos = reservaRepository.encontrarSolapamientos(
                    h.getId(), dto.getIngreso(), dto.getEgreso()
            );
            if (!conflictos.isEmpty()) {
                throw new RuntimeException("La habitación " + h.getNumero() + " ya está ocupada en esas fechas.");
            }
        }

        //Gestionar TITULAR (Quien paga)
        Huesped titular = obtenerOGuardarHuesped(dto.getHuesped());

        //Preparar mapa de ocupantes (si viene en el DTO)
        Map<Integer, List<OcupanteDTO>> mapaOcupantes = dto.getOcupantesPorHabitacion();

        //CREAR RESERVAS
        List<Reserva> reservasGuardadas = new ArrayList<>();

        for (Habitacion h : habitaciones) {
            Reserva r = new Reserva();
            r.setIngreso(dto.getIngreso());
            r.setEgreso(dto.getEgreso());
            r.setIdPersona(titular.getId()); // El titular va en la columna id_persona
            r.setEstado(estadoNuevo);
            r.setHabitacion(h);

            if (mapaOcupantes != null && mapaOcupantes.containsKey(h.getId())) {
                List<OcupanteDTO> listaDTOs = mapaOcupantes.get(h.getId());

                for (OcupanteDTO oDto : listaDTOs) {
                    // Solo procesamos si tiene nombre
                    if (oDto.getNombre() != null && !oDto.getNombre().isBlank()) {

                        Ocupante ocupante = new Ocupante();
                        ocupante.setNombre(oDto.getNombre());
                        ocupante.setApellido(oDto.getApellido());

                        // Asignamos el DNI (usando método setDni o setDocumento)
                        ocupante.setDni(oDto.getDni());
                        ocupante.setTelefono("-");
                        ocupante.setTipoDocumento(TipoDocumento.DNI);
                        ocupante.setHabitacion(h);
                        r.agregarOcupante(ocupante);
                    }
                }
            }
            reservasGuardadas.add(reservaRepository.save(r));
        }

        return reservasGuardadas;
    }

    /**
     * Obtiene un huésped por documento o lo crea si no existe.
     */
    private Huesped obtenerOGuardarHuesped(ReservaDTO.DatosHuespedReserva datos) {
        Huesped huesped = huespedRepository.findByDocumento(datos.getDocumento());
        if (huesped == null) {
            huesped = new Huesped();
            huesped.setNombre(datos.getNombre());
            huesped.setApellido(datos.getApellido());
            huesped.setDocumento(datos.getDocumento());
            huesped.setTelefono(datos.getTelefono());
            try {
                huesped.setTipoDocumento(TipoDocumento.valueOf(datos.getTipoDocumento().toUpperCase()));
            } catch (Exception e) {
                huesped.setTipoDocumento(TipoDocumento.DNI);
            }
            huesped.setFechaNacimiento(LocalDate.of(2000, 1, 1));
            huesped.setNacionalidad("Arg");
            huesped.setOcupacion("-");
            huesped.setEmail("temp_" + datos.getDocumento() + "@hotel.com");
            huesped.setPosicionIVA(RazonSocial.Consumidor_Final);
            Direccion d = new Direccion(); d.setCalle("-"); d.setNumero("-"); d.setLocalidad("-"); d.setProvincia("-"); d.setPais("Argentina"); d.setCodigoPostal("0");
            huesped.setDireccion(d);
            return huespedRepository.save(huesped);
        } else {
            if (datos.getTelefono() != null) {
                huesped.setTelefono(datos.getTelefono());
                return huespedRepository.save(huesped);
            }
        }
        return huesped;
    }
}