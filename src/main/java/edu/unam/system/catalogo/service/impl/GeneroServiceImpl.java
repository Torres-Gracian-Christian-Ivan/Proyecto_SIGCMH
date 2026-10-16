package edu.unam.system.catalogo.service.impl;

import edu.unam.system.catalogo.model.dto.GeneroDTO;
import edu.unam.system.catalogo.model.entity.Genero;
import edu.unam.system.catalogo.repository.GeneroRepository;
import edu.unam.system.catalogo.service.GeneroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneroServiceImpl implements GeneroService {

    private final GeneroRepository generoRepository;

    @Override
    public List<GeneroDTO> findAll() {

        return generoRepository.findAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    @Override
    public GeneroDTO findById(Integer id) {

        return generoRepository.findById(id)
                .map(this::convertirDTO)
                .orElse(null);
    }

    private GeneroDTO convertirDTO(Genero genero) {

        GeneroDTO dto = new GeneroDTO();
        dto.setIdGenero(genero.getIdGenero());
        dto.setNombre(genero.getNombre());

        return dto;
    }
}