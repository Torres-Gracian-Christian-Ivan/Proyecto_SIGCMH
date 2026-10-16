package edu.unam.system.catalogo.service;

import edu.unam.system.catalogo.model.dto.GeneroDTO;

import java.util.List;

public interface GeneroService {

    List<GeneroDTO> findAll();
    GeneroDTO findById(Integer id);

}
