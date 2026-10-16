package edu.unam.system.paciente.repository;

import edu.unam.system.paciente.model.entity.Paciente;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente,Integer>{
    long count();
    @EntityGraph(attributePaths = {"genero"})
    @Override
    List<Paciente> findAll();

    @EntityGraph(attributePaths = {"genero"})
    @Override
    Optional<Paciente> findById(Integer id);
    boolean existsByNumeroIdentificacion(String numeroIdentificacion);

    boolean existsByNumeroIdentificacionAndIdPacienteNot(
            String numeroIdentificacion, Integer idPaciente);

}