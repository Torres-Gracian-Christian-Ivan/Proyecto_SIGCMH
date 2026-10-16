package edu.unam.system.medico.repository;

import edu.unam.system.medico.model.entity.Medico;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {


    boolean existsByNumeroIdentificacion(
            String numeroIdentificacion
    );

    boolean existsByNumeroIdentificacionAndIdNot(
            String numeroIdentificacion,
            Integer id
    );

    @EntityGraph(attributePaths = {
            "especialidad",
            "consultorio"
    })
    @Override
    List<Medico> findAll();


    @EntityGraph(attributePaths = {
            "especialidad",
            "consultorio"
    })
    @Override
    Optional<Medico> findById(Integer id);
}