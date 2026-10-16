package edu.unam.auth.mapper;

import edu.unam.auth.model.dto.PerfilDTO;
import edu.unam.auth.model.entity.Perfil;
import org.springframework.stereotype.Component;

@Component
public class PerfilMapper {

    /**
     * Convierte una entidad Perfil a PerfilDTO
     */
    public PerfilDTO entityToDto(Perfil perfil) {

        PerfilDTO dto = new PerfilDTO();

        dto.setId(perfil.getId());
        dto.setNombre(perfil.getNombre());
        dto.setDescripcion(perfil.getDescripcion());
        dto.setActivo(perfil.getActivo());

        return dto;
    }

    /**
     * Convierte un PerfilDTO a entidad Perfil
     */
    public Perfil dtoToEntity(PerfilDTO dto) {

        Perfil perfil = new Perfil();

        perfil.setId(dto.getId());
        perfil.setNombre(dto.getNombre());
        perfil.setDescripcion(dto.getDescripcion());

        perfil.setActivo(
                dto.getActivo() != null ? dto.getActivo() : true
        );

        return perfil;
    }

}