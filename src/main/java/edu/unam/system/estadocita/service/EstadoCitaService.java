package edu.unam.system.estadocita.service;

import edu.unam.system.estadocita.model.dto.EstadoCitaDTO;

import java.util.List;

public interface EstadoCitaService {

    List<EstadoCitaDTO> findAll();
    EstadoCitaDTO findById(Integer id);
    void save(EstadoCitaDTO estadoDTO);
    void delete(Integer id);

}