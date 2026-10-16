package edu.unam.system.horarioatencion.mapper;

import edu.unam.system.horarioatencion.model.dto.HorarioAtencionDTO;
import edu.unam.system.horarioatencion.model.entity.HorarioAtencion;
import org.springframework.stereotype.Component;

@Component
public class HorarioAtencionMapper {

    public HorarioAtencionDTO entityToDto(HorarioAtencion horario) {

        HorarioAtencionDTO dto = new HorarioAtencionDTO();

        dto.setId(horario.getId());
        dto.setDiaSemana(horario.getDiaSemana());
        dto.setHoraInicio(horario.getHoraInicio());
        dto.setHoraFin(horario.getHoraFin());
        dto.setActivo(horario.getActivo());

        if (horario.getMedico() != null) {
            dto.setIdMedico(horario.getMedico().getId());
            dto.setNombreMedico(horario.getMedico().getNombreCompleto()
            );
        }

        return dto;
    }


    public HorarioAtencion dtoToEntity(HorarioAtencionDTO dto) {

        HorarioAtencion horario = new HorarioAtencion();

        horario.setId(dto.getId());
        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());

        horario.setActivo(
                dto.getActivo() != null
                        ? dto.getActivo()
                        : true
        );

        return horario;
    }
}