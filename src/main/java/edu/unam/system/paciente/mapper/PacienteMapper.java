package edu.unam.system.paciente.mapper;

import edu.unam.system.paciente.model.dto.PacienteDTO;
import edu.unam.system.catalogo.model.entity.Genero;
import edu.unam.system.paciente.model.entity.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

    /**
     * Convierte una entidad Paciente a PacienteDTO
     */
    public PacienteDTO entityToDto(Paciente paciente) {

        PacienteDTO dto = new PacienteDTO();

        dto.setIdPaciente(paciente.getIdPaciente());
        dto.setNombreCompleto(paciente.getNombreCompleto());
        dto.setNumeroIdentificacion(paciente.getNumeroIdentificacion());
        dto.setFechaNacimiento(paciente.getFechaNacimiento());
        dto.setTelefono(paciente.getTelefono());
        dto.setEmail(paciente.getEmail());
        dto.setDireccion(paciente.getDireccion());
        dto.setActivo(paciente.getActivo());

        if (paciente.getGenero() != null) {
            dto.setIdGenero(paciente.getGenero().getIdGenero());
            dto.setGenero(paciente.getGenero().getNombre());
        }

        return dto;
    }

    /**
     * Convierte un PacienteDTO a entidad Paciente
     */
    public Paciente dtoToEntity(PacienteDTO dto) {

        Paciente paciente = new Paciente();

        if (dto.getIdPaciente() != null) {
            paciente.setIdPaciente(dto.getIdPaciente());
        }

        paciente.setNombreCompleto(dto.getNombreCompleto());
        paciente.setNumeroIdentificacion(dto.getNumeroIdentificacion());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setTelefono(dto.getTelefono());
        paciente.setEmail(dto.getEmail());
        paciente.setDireccion(dto.getDireccion());
        paciente.setActivo(dto.getActivo() != null ? dto.getActivo() : true);

        if (dto.getIdGenero() != null) {
            Genero genero = new Genero();
            genero.setIdGenero(dto.getIdGenero());
            paciente.setGenero(genero);
        }

        return paciente;
    }

}
