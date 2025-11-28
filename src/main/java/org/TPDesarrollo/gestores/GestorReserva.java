package org.TPDesarrollo.gestores;

import org.TPDesarrollo.clases.Habitacion;
import org.TPDesarrollo.clases.Reserva;
import org.TPDesarrollo.clases.Huesped;
import org.TPDesarrollo.clases.Direccion;
import org.TPDesarrollo.dtos.ReservaDTO;
import org.TPDesarrollo.enums.EstadoHabitacion;
import org.TPDesarrollo.enums.TipoDocumento;
import org.TPDesarrollo.enums.RazonSocial;
import org.TPDesarrollo.repository.HuespedRepository;
import org.TPDesarrollo.repository.HabitacionRepository;
import org.TPDesarrollo.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class GestorReserva {

    private final HabitacionRepository habitacionRepository;
    private final ReservaRepository reservaRepository;
    private final HuespedRepository huespedRepository;

    public GestorReserva(HabitacionRepository hr, ReservaRepository rr, HuespedRepository phr) {
        this.habitacionRepository = hr;
        this.reservaRepository = rr;
        this.huespedRepository = phr;
    }

    // 1. RESERVAR
    @Transactional
    public List<Reserva> crearReserva(ReservaDTO dto) {
        return procesarReserva(dto, "RESERVADA");
    }

    // 2. OCUPAR (CHECK-IN)
    @Transactional
    public List<Reserva> crearOcupacion(ReservaDTO dto) {
        return procesarReserva(dto, "OCUPADA");
    }

    // --- LÓGICA CENTRAL ---
    private List<Reserva> procesarReserva(ReservaDTO dto, String estadoNuevo) {

        // Validar fechas
        if (dto.getIngreso().isAfter(dto.getEgreso())) {
            throw new IllegalArgumentException("La fecha de ingreso debe ser anterior al egreso");
        }

        // Obtener Habitaciones solicitadas
        List<Habitacion> habitaciones = habitacionRepository.findAllById(dto.getHabitaciones());
        if (habitaciones.size() != dto.getHabitaciones().size()) {
            throw new RuntimeException("Alguna habitación solicitada no existe");
        }

        // VALIDAR DISPONIBILIDAD (Consultando la tabla Reserva)
        for (Habitacion h : habitaciones) {
            List<Reserva> conflictos = reservaRepository.encontrarSolapamientos(
                    h.getId(), dto.getIngreso(), dto.getEgreso()
            );
            if (!conflictos.isEmpty()) {
                throw new RuntimeException("La habitación " + h.getNumero() + " ya está ocupada en esas fechas.");
            }
        }

        // Gestionar Huésped
        Huesped huesped = obtenerOGuardarHuesped(dto.getHuesped());

        // CREAR LAS RESERVAS (Una fila por cada habitación)
        List<Reserva> reservasGuardadas = new ArrayList<>();

        for (Habitacion h : habitaciones) {
            Reserva r = new Reserva();
            r.setIngreso(dto.getIngreso());
            r.setEgreso(dto.getEgreso());
            r.setIdPersona(huesped.getId());
            r.setEstado(estadoNuevo);
            r.setHabitacion(h); // Guardamos FK id_habitacion

            // SOLO GUARDAMOS LA RESERVA. NO TOCAMOS LA HABITACIÓN.
            reservasGuardadas.add(reservaRepository.save(r));
        }

        return reservasGuardadas;
    }

    // Auxiliar Huesped
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
            // Defaults obligatorios
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