package edu.unam.auth.service;

import edu.unam.auth.model.dto.PerfilDTO;

import java.util.List;

public interface PerfilService {

    List<PerfilDTO> findAll();
    PerfilDTO findById(Integer id);
    void save(PerfilDTO perfilDTO);
    void delete(Integer id);

}