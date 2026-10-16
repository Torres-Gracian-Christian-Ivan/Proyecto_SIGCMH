package edu.unam.auth.service.impl;

import edu.unam.auth.mapper.UsuarioMapper;
import edu.unam.auth.model.dto.UsuarioDTO;
import edu.unam.auth.model.entity.Perfil;
import edu.unam.auth.model.entity.Usuario;
import edu.unam.auth.repository.PerfilRepository;
import edu.unam.auth.repository.UsuarioRepository;
import edu.unam.auth.service.UsuarioService;

import edu.unam.common.exception.DuplicateResourceException;
import edu.unam.common.exception.ResourceNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class UsuarioServiceImpl
        implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final UsuarioMapper usuarioMapper;

    private final PerfilRepository perfilRepository;

    private final PasswordEncoder passwordEncoder;


    public UsuarioServiceImpl(
            UsuarioRepository usuarioRepository,
            UsuarioMapper usuarioMapper,
            PerfilRepository perfilRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository =
                usuarioRepository;

        this.usuarioMapper =
                usuarioMapper;

        this.perfilRepository =
                perfilRepository;

        this.passwordEncoder =
                passwordEncoder;
    }


    @Override
    public Long count() {

        return usuarioRepository.count();
    }


    @Override
    public List<UsuarioDTO> findAll() {

        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::entityToDto)
                .toList();
    }


    @Override
    public UsuarioDTO findById(
            Integer id) {

        Usuario usuario =
                usuarioRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "usuario.notfound"
                                )
                        );

        return usuarioMapper
                .entityToDto(usuario);
    }


    @Override
    @Transactional
    public UsuarioDTO save(
            UsuarioDTO dto) {

        if (dto.getId() == null) {

            return create(dto);
        }

        update(dto);

        return findById(
                dto.getId()
        );
    }


    @Override
    @Transactional
    public UsuarioDTO create(
            UsuarioDTO dto) {

        validarPasswordNuevo(
                dto.getPasswordHash()
        );


        if (usuarioRepository
                .existsByEmail(dto.getEmail())) {

            throw new DuplicateResourceException(
                    "usuario.duplicate"
            );
        }


        Perfil perfil =
                perfilRepository
                        .findById(dto.getIdPerfil())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "perfil.notfound"
                                )
                        );


        Usuario usuario =
                usuarioMapper
                        .dtoToEntity(dto);


        usuario.setPasswordHash(
                passwordEncoder.encode(
                        dto.getPasswordHash()
                )
        );


        usuario.setPerfil(
                perfil
        );


        usuario.setId(null);


        if (usuario.getActivo() == null) {

            usuario.setActivo(true);
        }


        Usuario guardado =
                usuarioRepository
                        .save(usuario);


        return usuarioMapper
                .entityToDto(guardado);
    }

    @Override
    @Transactional
    public void update(
            UsuarioDTO dto) {

        Usuario usuario =
                usuarioRepository
                        .findById(dto.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "usuario.notfound"
                                )
                        );


        if (usuarioRepository
                .existsByEmailAndIdNot(
                        dto.getEmail(),
                        dto.getId()
                )) {

            throw new DuplicateResourceException(
                    "usuario.duplicate"
            );
        }


        Perfil perfil =
                perfilRepository
                        .findById(dto.getIdPerfil())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "perfil.notfound"
                                )
                        );


        usuario.setNombreCompleto(
                dto.getNombreCompleto()
        );


        usuario.setEmail(
                dto.getEmail()
        );


        usuario.setPerfil(
                perfil
        );


        usuario.setActivo(
                dto.getActivo() != null
                        ? dto.getActivo()
                        : false
        );


        /*
         * Solo cambia la contraseña
         * cuando el administrador escribió
         * una contraseña nueva.
         */
        if (dto.getPasswordHash() != null
                && !dto.getPasswordHash()
                .isBlank()) {

            validarPassword(
                    dto.getPasswordHash()
            );


            usuario.setPasswordHash(
                    passwordEncoder.encode(
                            dto.getPasswordHash()
                    )
            );
        }


        usuarioRepository.save(
                usuario
        );
    }


    @Override
    @Transactional
    public void delete(
            Integer id) {

        Usuario usuario =
                usuarioRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "usuario.notfound"
                                )
                        );


        /*
         * No eliminar físicamente porque
         * puede estar relacionado con citas.
         */
        usuario.setActivo(false);

        usuarioRepository.save(
                usuario
        );
    }


    // =========================================
    // validar pass
    // =========================================

    private void validarPasswordNuevo(
            String password) {

        if (password == null
                || password.isBlank()) {

            throw new IllegalArgumentException(
                    "usuario.password.required"
            );
        }


        validarPassword(password);
    }


    private void validarPassword(
            String password) {

        if (password.length() < 8
                || password.length() > 255) {

            throw new IllegalArgumentException(
                    "usuario.password.size"
            );
        }
    }
}