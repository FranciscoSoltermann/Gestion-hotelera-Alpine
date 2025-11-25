package org.TPDesarrollo.repository;

import org.TPDesarrollo.clases.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Integer> {
}