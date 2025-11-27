package org.TPDesarrollo.gestores;

import org.TPDesarrollo.clases.Habitacion;
import org.TPDesarrollo.clases.Reserva;
import org.TPDesarrollo.clases.Huesped;
import org.TPDesarrollo.clases.Direccion; // <--- IMPORTANTE: Importa tu clase Direccion
import org.TPDesarrollo.dtos.ReservaDTO;
import org.TPDesarrollo.enums.EstadoHabitacion;
import org.TPDesarrollo.enums.TipoDocumento; // Importa tus Enums
import org.TPDesarrollo.enums.RazonSocial;   // Importa tus Enums
import org.TPDesarrollo.repository.HuespedRepository;
import org.TPDesarrollo.repository.HabitacionRepository;
import org.TPDesarrollo.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Transactional
    public Reserva crearReserva(ReservaDTO dto) {

        // 1. Validar fechas
        if (dto.getIngreso().isAfter(dto.getEgreso())) {
            throw new IllegalArgumentException("La fecha de ingreso debe ser anterior al egreso");
        }

        // 2. Validar habitaciones
        List<Integer> ids = dto.getHabitaciones();
        List<Habitacion> habitaciones = (List<Habitacion>) habitacionRepository.findAllById(ids);

        if (habitaciones.size() != ids.size()) {
            throw new RuntimeException("Alguna habitación solicitada no existe");
        }

        // 3. Validar solapamientos
        LocalDate desde = dto.getIngreso();
        LocalDate hasta = dto.getEgreso();

        for (Habitacion h : habitaciones) {
            if (h.getReservaActual() != null) {
                Reserva rExistente = h.getReservaActual();
                LocalDate rDesde = rExistente.getIngreso();
                LocalDate rHasta = rExistente.getEgreso();
                // Si se solapan las fechas...
                if (!(rHasta.isBefore(desde) || rDesde.isAfter(hasta))) {
                    throw new RuntimeException("Habitación " + h.getNumero() + " ocupada en esas fechas.");
                }
            }
        }

        // 4. LOGICA DE HUESPED
        ReservaDTO.DatosHuespedReserva datosHuesped = dto.getHuesped();
        Huesped huesped = huespedRepository.findByDocumento(datosHuesped.getDocumento());

        if (huesped == null) {
            // --- CREACIÓN DE HUESPED CON DATOS MÍNIMOS ---
            huesped = new Huesped();

            // A) Datos que vinieron del Front
            huesped.setNombre(datosHuesped.getNombre());
            huesped.setApellido(datosHuesped.getApellido());
            huesped.setDocumento(datosHuesped.getDocumento());
            huesped.setTelefono(datosHuesped.getTelefono());

            // Intentar convertir el Enum de TipoDocumento
            try {
                // Asegúrate que coincida mayúsculas/minúsculas con tu Enum
                huesped.setTipoDocumento(TipoDocumento.valueOf(datosHuesped.getTipoDocumento().toUpperCase()));
            } catch (Exception e) {
                huesped.setTipoDocumento(TipoDocumento.DNI); // Default si falla
            }

            // B) RELLENO DE DATOS OBLIGATORIOS (Para que la BD no falle)
            // Estos datos el usuario los actualizará después en el Check-In

            huesped.setFechaNacimiento(LocalDate.of(2000, 1, 1)); // Fecha válida cualquiera
            huesped.setNacionalidad("A Completar");
            huesped.setOcupacion("A Completar");
            huesped.setEmail("temp_" + datosHuesped.getDocumento() + "@hotel.com"); // Email temporal único
            huesped.setPosicionIVA(RazonSocial.Consumidor_Final);

            // Si Direccion es obligatoria, crea una vacía:
            Direccion dirDummy = new Direccion();
            dirDummy.setCalle("Sin calle");
            dirDummy.setNumero("-");
            dirDummy.setLocalidad("Sin ciudad");
            dirDummy.setProvincia("Sin provincia");
            dirDummy.setPais("Argentina");
            dirDummy.setCodigoPostal("0000");

            huesped.setDireccion(dirDummy);

            // Guardamos
            huesped = huespedRepository.save(huesped);
        } else {
            // SI EXISTE: Actualizamos teléfono si vino nuevo
            if (datosHuesped.getTelefono() != null && !datosHuesped.getTelefono().isEmpty()) {
                huesped.setTelefono(datosHuesped.getTelefono());
                huespedRepository.save(huesped);
            }
        }

        // 5. Crear Reserva
        Reserva reserva = new Reserva();
        reserva.setIngreso(desde);
        reserva.setEgreso(hasta);
        reserva.setIdPersona(huesped.getId());
        reserva.setHabitaciones(habitaciones);

        Reserva saved = reservaRepository.save(reserva);

        // 6. Actualizar habitaciones
        for (Habitacion h : habitaciones) {
            h.setReservaActual(saved);
            h.setIngreso(desde);
            h.setEgreso(hasta);
            h.setEstado(EstadoHabitacion.RESERVADA);
        }
        habitacionRepository.saveAll(habitaciones);

        return saved;
    }
}