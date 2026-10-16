package edu.unam.system.cita.mapper;

import edu.unam.system.cita.model.dto.CitaDTO;
import edu.unam.system.cita.model.entity.Cita;
import org.springframework.stereotype.Component;

@Component
public class CitaMapper {

    public CitaDTO entityToDto(Cita cita) {

        if (cita == null) {
            return null;
        }

        CitaDTO dto = new CitaDTO();

        dto.setId(cita.getId());

        // Paciente
        if (cita.getPaciente() != null) {
            dto.setIdPaciente(cita.getPaciente().getIdPaciente());
            dto.setNombrePaciente(cita.getPaciente().getNombreCompleto());
        }

        // Médico
        if (cita.getMedico() != null) {
            dto.setIdMedico(cita.getMedico().getId());
            dto.setNombreMedico(cita.getMedico().getNombreCompleto());
        }

        // Usuario creador
        if (cita.getUsuarioCreador() != null) {
            dto.setIdUsuarioCreador(cita.getUsuarioCreador().getId());
            dto.setNombreUsuario(cita.getUsuarioCreador().getNombreCompleto());
        }

        // Estado
        if (cita.getEstado() != null) {
            dto.setIdEstado(cita.getEstado().getId());
            dto.setEstado(cita.getEstado().getNombre());
        }

        dto.setFechaHora(cita.getFechaHora());
        dto.setMotivoConsulta(cita.getMotivoConsulta());
        dto.setObservaciones(cita.getObservaciones());

        return dto;
    }

    public Cita dtoToEntity(CitaDTO dto) {

        if (dto == null) {
            return null;
        }

        Cita cita = new Cita();

        cita.setId(dto.getId());

        // Las relaciones (Paciente, Médico, Usuario y Estado)
        // se asignan en el Service.

        cita.setFechaHora(dto.getFechaHora());
        cita.setMotivoConsulta(dto.getMotivoConsulta());
        cita.setObservaciones(dto.getObservaciones());

        return cita;
    }
}