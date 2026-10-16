package edu.unam.system.cita.repository;

import edu.unam.system.cita.model.entity.Cita;
import edu.unam.system.medico.model.entity.Medico;
import edu.unam.system.paciente.model.entity.Paciente;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CitaRepository
        extends JpaRepository<Cita, Integer> {

    // CONSULTAS GENERALES
    @EntityGraph(attributePaths = {
            "paciente",
            "medico",
            "estado",
            "usuarioCreador"
    })
    @Override
    List<Cita> findAll();


    @EntityGraph(attributePaths = {
            "paciente",
            "medico",
            "estado",
            "usuarioCreador"
    })
    @Override
    Optional<Cita> findById(Integer id);

    // VALIDACIONES DE DISPONIBILIDAD
    boolean existsByMedicoIdAndFechaHora(
            Integer idMedico,
            LocalDateTime fechaHora
    );


    boolean existsByPacienteIdPacienteAndFechaHora(
            Integer idPaciente,
            LocalDateTime fechaHora
    );

    // CONSULTAS POR MÉDICO / PACIENTE / FECHA
    @EntityGraph(attributePaths = {
            "paciente",
            "medico",
            "estado",
            "usuarioCreador"
    })
    List<Cita> findByMedicoId(
            Integer idMedico
    );


    @EntityGraph(attributePaths = {
            "paciente",
            "medico",
            "estado",
            "usuarioCreador"
    })
    List<Cita> findByPacienteIdPaciente(
            Integer idPaciente
    );


    @EntityGraph(attributePaths = {
            "paciente",
            "medico",
            "estado",
            "usuarioCreador"
    })
    List<Cita> findByFechaHoraBetween(
            LocalDateTime inicio,
            LocalDateTime fin
    );

    // RELACIÓN N:M PACIENTE - MÉDICO
    // Obtener médicos relacionados con un paciente
    // mediante sus citas.
    @Query("""
           SELECT DISTINCT c.medico
           FROM Cita c
           WHERE c.paciente.idPaciente = :idPaciente
           """)
    List<Medico> findMedicosByPacienteId(
            @Param("idPaciente") Integer idPaciente
    );


    // Obtener pacientes relacionados con un médico
    // mediante sus citas.
    @Query("""
           SELECT DISTINCT c.paciente
           FROM Cita c
           WHERE c.medico.id = :idMedico
           """)
    List<Paciente> findPacientesByMedicoId(
            @Param("idMedico") Integer idMedico
    );
}