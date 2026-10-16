package edu.unam.system.horarioatencion.repository;

import edu.unam.system.horarioatencion.model.entity.HorarioAtencion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;

public interface HorarioAtencionRepository
        extends JpaRepository<HorarioAtencion, Integer> {

    List<HorarioAtencion>
    findByMedicoIdOrderByDiaSemanaAscHoraInicioAsc(
            Integer idMedico
    );

    boolean existsByMedicoIdAndDiaSemanaAndHoraInicio(
            Integer idMedico,
            Integer diaSemana,
            LocalTime horaInicio
    );

    boolean existsByMedicoIdAndDiaSemanaAndHoraInicioAndIdNot(
            Integer idMedico,
            Integer diaSemana,
            LocalTime horaInicio,
            Integer idHorario
    );

    boolean existsByMedicoIdAndDiaSemanaAndHoraInicioLessThanAndHoraFinGreaterThan(
            Integer idMedico,
            Integer diaSemana,
            LocalTime horaFin,
            LocalTime horaInicio
    );

    boolean existsByMedicoIdAndDiaSemanaAndHoraInicioLessThanAndHoraFinGreaterThanAndIdNot(
            Integer idMedico,
            Integer diaSemana,
            LocalTime horaFin,
            LocalTime horaInicio,
            Integer id
    );
}