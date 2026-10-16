package edu.unam.auth.service.impl;

import edu.unam.common.exception.DuplicateResourceException;
import edu.unam.common.exception.ResourceNotFoundException;
import edu.unam.auth.mapper.PerfilMapper;
import edu.unam.auth.model.dto.PerfilDTO;
import edu.unam.auth.model.entity.Perfil;
import edu.unam.auth.repository.PerfilRepository;
import edu.unam.auth.service.PerfilService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilServiceImpl implements PerfilService {

    private final PerfilRepository perfilRepository;
    private final PerfilMapper perfilMapper;

    public PerfilServiceImpl(PerfilRepository perfilRepository,
                             PerfilMapper perfilMapper) {
        this.perfilRepository = perfilRepository;
        this.perfilMapper = perfilMapper;
    }

    @Override
    public List<PerfilDTO> findAll() {

        return perfilRepository.findAll()
                .stream()
                .map(perfilMapper::entityToDto)
                .toList();

    }

    @Override
    public PerfilDTO findById(Integer id) {

        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "perfil.notfound"));

        return perfilMapper.entityToDto(perfil);

    }

    @Override
    public void save(PerfilDTO perfilDTO) {

        if (perfilDTO.getId() == null &&
                perfilRepository.existsByNombre(
                        perfilDTO.getNombre())) {

            throw new DuplicateResourceException(
                    "perfil.duplicate");

        }

        Perfil perfil =
                perfilMapper.dtoToEntity(perfilDTO);

        perfilRepository.save(perfil);

    }
    @Override
    public void delete(Integer id) {

        if (!perfilRepository.existsById(id)) {

            throw new ResourceNotFoundException(
                    "perfil.notfound");

        }
        perfilRepository.deleteById(id);

    }

}