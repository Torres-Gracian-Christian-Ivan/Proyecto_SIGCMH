package edu.unam.auth.mapper;

import edu.unam.auth.model.dto.UsuarioDTO;
import edu.unam.auth.model.entity.Perfil;
import edu.unam.auth.model.entity.Usuario;

import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioDTO entityToDto(
            Usuario usuario) {

        if (usuario == null) {
            return null;
        }

        UsuarioDTO dto =
                new UsuarioDTO();

        dto.setId(
                usuario.getId()
        );

        dto.setNombreCompleto(
                usuario.getNombreCompleto()
        );

        dto.setEmail(
                usuario.getEmail()
        );

        /*
         * IMPORTANTE:
         * Nunca devolver el hash BCrypt.
         */
        dto.setPasswordHash(null);

        dto.setActivo(
                usuario.getActivo()
        );

        if (usuario.getPerfil() != null) {

            dto.setIdPerfil(
                    usuario.getPerfil().getId()
            );

            dto.setPerfil(
                    usuario.getPerfil().getNombre()
            );
        }

        return dto;
    }


    public Usuario dtoToEntity(
            UsuarioDTO dto) {

        if (dto == null) {
            return null;
        }

        Usuario usuario =
                new Usuario();

        usuario.setId(
                dto.getId()
        );

        usuario.setNombreCompleto(
                dto.getNombreCompleto()
        );

        usuario.setEmail(
                dto.getEmail()
        );

        usuario.setActivo(
                dto.getActivo() != null
                        ? dto.getActivo()
                        : true
        );

        if (dto.getIdPerfil() != null) {

            Perfil perfil =
                    new Perfil();

            perfil.setId(
                    dto.getIdPerfil()
            );

            usuario.setPerfil(
                    perfil
            );
        }

        return usuario;
    }
}