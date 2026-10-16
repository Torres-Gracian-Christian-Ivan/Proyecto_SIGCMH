package edu.unam.auth.service;

import edu.unam.auth.model.dto.UsuarioDTO;

import java.util.List;

public interface UsuarioService {
    Long count();
    List<UsuarioDTO> findAll();
    UsuarioDTO findById(Integer id);
    UsuarioDTO save(UsuarioDTO usuarioDTO);
    UsuarioDTO create(UsuarioDTO usuarioDTO);
    void update(UsuarioDTO usuarioDTO);
    void delete(Integer id);
}