package edu.unam.system.estadocita.repository;

import edu.unam.system.estadocita.model.entity.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoCitaRepository extends JpaRepository<EstadoCita, Integer> {

    boolean existsByNombre(String nombre);

}
