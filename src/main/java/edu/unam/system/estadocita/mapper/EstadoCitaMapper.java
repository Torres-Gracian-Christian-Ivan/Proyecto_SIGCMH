package edu.unam.system.estadocita.mapper;


import edu.unam.system.estadocita.model.dto.EstadoCitaDTO;
import edu.unam.system.estadocita.model.entity.EstadoCita;
import org.springframework.stereotype.Component;

@Component
public class EstadoCitaMapper {

    /**
     * Convierte una entidad EstadoCita a EstadoCitaDTO
     */
    public EstadoCitaDTO entityToDto(EstadoCita estado) {

        EstadoCitaDTO dto = new EstadoCitaDTO();
        dto.setId(estado.getId());
        dto.setNombre(estado.getNombre());
        dto.setActivo(estado.getActivo());
        return dto;

    }

    /**
     * Convierte un EstadoCitaDTO a entidad EstadoCita
     */
    public EstadoCita dtoToEntity(EstadoCitaDTO dto) {

        EstadoCita estado = new EstadoCita();
        estado.setId(dto.getId());
        estado.setNombre(dto.getNombre());
        estado.setActivo(
                dto.getActivo() != null ? dto.getActivo() : true
        );
        return estado;

    }

}
