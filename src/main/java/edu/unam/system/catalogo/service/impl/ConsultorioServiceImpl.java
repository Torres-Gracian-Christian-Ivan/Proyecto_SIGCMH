package edu.unam.system.catalogo.service.impl;

import edu.unam.system.catalogo.model.dto.ConsultorioDTO;
import edu.unam.system.catalogo.model.entity.Consultorio;
import edu.unam.system.catalogo.repository.ConsultorioRepository;
import edu.unam.system.catalogo.service.ConsultorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultorioServiceImpl implements ConsultorioService {

    private final ConsultorioRepository consultorioRepository;

    @Override
    public List<ConsultorioDTO> findAll() {

        return consultorioRepository.findAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    @Override
    public ConsultorioDTO findById(Integer id) {

        return consultorioRepository.findById(id)
                .map(this::convertirDTO)
                .orElse(null);
    }

    private ConsultorioDTO convertirDTO(Consultorio consultorio){

        ConsultorioDTO dto = new ConsultorioDTO();

        dto.setId(consultorio.getId());
        dto.setNumero(consultorio.getNumero());
        dto.setUbicacion(consultorio.getUbicacion());
        dto.setActivo(consultorio.getActivo());

        return dto;
    }
}