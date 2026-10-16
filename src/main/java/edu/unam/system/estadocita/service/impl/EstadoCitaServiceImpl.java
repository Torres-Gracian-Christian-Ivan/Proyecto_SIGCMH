package edu.unam.system.estadocita.service.impl;

import edu.unam.common.exception.DuplicateResourceException;
import edu.unam.common.exception.ResourceNotFoundException;
import edu.unam.system.estadocita.mapper.EstadoCitaMapper;
import edu.unam.system.estadocita.model.dto.EstadoCitaDTO;
import edu.unam.system.estadocita.model.entity.EstadoCita;
import edu.unam.system.estadocita.repository.EstadoCitaRepository;
import edu.unam.system.estadocita.service.EstadoCitaService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EstadoCitaServiceImpl
        implements EstadoCitaService {

    private final EstadoCitaRepository estadoRepository;
    private final EstadoCitaMapper estadoConverter;

    public EstadoCitaServiceImpl(
            EstadoCitaRepository estadoRepository,
            EstadoCitaMapper estadoConverter) {

        this.estadoRepository = estadoRepository;
        this.estadoConverter = estadoConverter;
    }


    @Override
    public List<EstadoCitaDTO> findAll() {

        return estadoRepository.findAll()
                .stream()
                .map(estadoConverter::entityToDto)
                .toList();
    }


    @Override
    public EstadoCitaDTO findById(Integer id) {

        EstadoCita estado =
                estadoRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "estadocita.notfound"
                                )
                        );

        return estadoConverter.entityToDto(estado);
    }


    @Override
    @Transactional
    public void save(EstadoCitaDTO estadoDTO) {

        if (estadoDTO.getId() == null
                && estadoRepository.existsByNombre(
                estadoDTO.getNombre())) {

            throw new DuplicateResourceException(
                    "estadocita.duplicate"
            );
        }


        EstadoCita estado =
                estadoConverter.dtoToEntity(
                        estadoDTO
                );


        estadoRepository.save(estado);
    }


    @Override
    @Transactional
    public void delete(Integer id) {

        if (!estadoRepository.existsById(id)) {

            throw new ResourceNotFoundException(
                    "estadocita.notfound"
            );
        }


        estadoRepository.deleteById(id);
    }
}