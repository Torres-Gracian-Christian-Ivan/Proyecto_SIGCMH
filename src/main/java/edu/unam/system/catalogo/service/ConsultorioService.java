package edu.unam.system.catalogo.service;

import edu.unam.system.catalogo.model.dto.ConsultorioDTO;

import java.util.List;

public interface ConsultorioService {

    List<ConsultorioDTO> findAll();
    ConsultorioDTO findById(Integer id);

}