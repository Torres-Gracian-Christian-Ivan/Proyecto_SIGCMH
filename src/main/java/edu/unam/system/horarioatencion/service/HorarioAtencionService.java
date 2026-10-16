package edu.unam.system.horarioatencion.service;

import edu.unam.system.horarioatencion.model.dto.HorarioAtencionDTO;

import java.util.List;

public interface HorarioAtencionService {

    List<HorarioAtencionDTO> findByMedico(Integer idMedico);

    HorarioAtencionDTO findById(
            Integer idMedico,
            Integer idHorario
    );

    HorarioAtencionDTO save(
            Integer idMedico,
            HorarioAtencionDTO dto
    );

    HorarioAtencionDTO update(
            Integer idMedico,
            Integer idHorario,
            HorarioAtencionDTO dto
    );

    void delete(
            Integer idMedico,
            Integer idHorario
    );
}