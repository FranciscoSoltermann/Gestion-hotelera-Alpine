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

        // 1. Validar fechas
        if (dto.getIngreso().isAfter(dto.getEgreso())) {
            throw new IllegalArgumentException("La fecha de ingreso debe ser anterior al egreso");
        }

        // 2. Obtener Habitaciones solicitadas
        List<Habitacion> habitaciones = habitacionRepository.findAllById(dto.getHabitaciones());
        if (habitaciones.size() != dto.getHabitaciones().size()) {
            throw new RuntimeException("Alguna habitación solicitada no existe");
        }

        // 3. VALIDAR DISPONIBILIDAD
        for (Habitacion h : habitaciones) {
            List<Reserva> conflictos = reservaRepository.encontrarSolapamientos(
                    h.getId(), dto.getIngreso(), dto.getEgreso()
            );
            if (!conflictos.isEmpty()) {
                throw new RuntimeException("La habitación " + h.getNumero() + " ya está ocupada en esas fechas.");
            }
        }

        // 4. Gestionar TITULAR (Quien paga)
        Huesped titular = obtenerOGuardarHuesped(dto.getHuesped());

        // 5. Preparar mapa de ocupantes (si viene en el DTO)
        Map<Integer, List<OcupanteDTO>> mapaOcupantes = dto.getOcupantesPorHabitacion();

        // 6. CREAR RESERVAS
        List<Reserva> reservasGuardadas = new ArrayList<>();

        for (Habitacion h : habitaciones) {
            Reserva r = new Reserva();
            r.setIngreso(dto.getIngreso());
            r.setEgreso(dto.getEgreso());
            r.setIdPersona(titular.getId()); // El titular va en la columna id_persona
            r.setEstado(estadoNuevo);
            r.setHabitacion(h);

            // --- [IMPORTANTE] LÓGICA DE OCUPANTES AÑADIDA ---
            if (mapaOcupantes != null && mapaOcupantes.containsKey(h.getId())) {
                List<OcupanteDTO> listaDTOs = mapaOcupantes.get(h.getId());

                // Opcional: Validar capacidad aquí si quieres ser estricto
                // if (listaDTOs.size() > h.getCapacidad()) throw ...

                for (OcupanteDTO oDto : listaDTOs) {
                    // Solo agregamos si tiene nombre (evitar filas vacías)
                    if (oDto.getNombre() != null && !oDto.getNombre().isBlank()) {
                        Ocupante ocupante = new Ocupante();
                        ocupante.setNombre(oDto.getNombre());
                        ocupante.setApellido(oDto.getApellido());

                        // Usamos el método puente setDni (que llama a setDocumento en Persona)
                        ocupante.setDni(oDto.getDni());

                        // Método helper que vincula la Reserva al Ocupante
                        r.agregarOcupante(ocupante);
                    }
                }
            }
            // --------------------------------------------------

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