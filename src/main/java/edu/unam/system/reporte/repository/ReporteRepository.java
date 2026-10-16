package edu.unam.system.reporte.repository;

import edu.unam.system.cita.model.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReporteRepository
        extends JpaRepository<Cita, Integer> {

    @Query("""
        SELECT c
        FROM Cita c
        JOIN FETCH c.paciente p
        JOIN FETCH c.medico m
        JOIN FETCH m.especialidad e
        JOIN FETCH m.consultorio co
        JOIN FETCH c.estado ec
        WHERE (:fechaInicio IS NULL OR c.fechaHora >= :fechaInicio)
          AND (:fechaFin IS NULL OR c.fechaHora < :fechaFin)
          AND (:idMedico IS NULL OR m.id = :idMedico)
          AND (:idPaciente IS NULL OR p.idPaciente = :idPaciente)
          AND (:idEstado IS NULL OR ec.id = :idEstado)
          AND (:idEspecialidad IS NULL OR e.id = :idEspecialidad)
          AND (:idConsultorio IS NULL OR co.id = :idConsultorio)
        ORDER BY c.fechaHora ASC
    """)
    List<Cita> buscarReporte(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            @Param("idMedico") Integer idMedico,
            @Param("idPaciente") Integer idPaciente,
            @Param("idEstado") Integer idEstado,
            @Param("idEspecialidad") Integer idEspecialidad,
            @Param("idConsultorio") Integer idConsultorio
    );

    @Query("""
    SELECT c
    FROM Cita c
    JOIN FETCH c.paciente p
    JOIN FETCH c.medico m
    JOIN FETCH m.especialidad e
    JOIN FETCH m.consultorio co
    JOIN FETCH c.estado ec
    WHERE co.id = :idConsultorio
      AND c.fechaHora >= :inicioSemana
      AND c.fechaHora < :finSemana
    ORDER BY c.fechaHora ASC
    """)
    List<Cita> buscarAgendaSemanal(
            @Param("idConsultorio")
            Integer idConsultorio,

            @Param("inicioSemana")
            LocalDateTime inicioSemana,

            @Param("finSemana")
            LocalDateTime finSemana
    );
}