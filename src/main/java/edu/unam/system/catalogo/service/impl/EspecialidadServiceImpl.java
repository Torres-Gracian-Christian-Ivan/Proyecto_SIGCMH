package edu.unam.system.catalogo.service.impl;

import edu.unam.system.catalogo.model.dto.EspecialidadDTO;
import edu.unam.system.catalogo.model.entity.Especialidad;
import edu.unam.system.catalogo.repository.EspecialidadRepository;
import edu.unam.system.catalogo.service.EspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspecialidadServiceImpl implements EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    @Override
    public List<EspecialidadDTO> findAll() {

        return especialidadRepository.findAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    @Override
    public EspecialidadDTO findById(Integer id) {

        return especialidadRepository.findById(id)
                .map(this::convertirDTO)
                .orElse(null);
    }

    private EspecialidadDTO convertirDTO(Especialidad especialidad){

        EspecialidadDTO dto = new EspecialidadDTO();

        dto.setId(especialidad.getId());
        dto.setNombre(especialidad.getNombre());
        dto.setDescripcion(especialidad.getDescripcion());

        return dto;
    }
}