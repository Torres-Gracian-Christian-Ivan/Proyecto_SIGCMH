package edu.unam.system.catalogo.service;

import edu.unam.system.catalogo.model.dto.EspecialidadDTO;

import java.util.List;

public interface EspecialidadService {
    List<EspecialidadDTO> findAll();
    EspecialidadDTO findById(Integer id);

}