package edu.unam.system.horarioatencion.service.impl;

import edu.unam.common.exception.DuplicateResourceException;
import edu.unam.common.exception.ResourceNotFoundException;
import edu.unam.system.horarioatencion.mapper.HorarioAtencionMapper;
import edu.unam.system.horarioatencion.model.dto.HorarioAtencionDTO;
import edu.unam.system.horarioatencion.model.entity.HorarioAtencion;
import edu.unam.system.medico.model.entity.Medico;
import edu.unam.system.horarioatencion.repository.HorarioAtencionRepository;
import edu.unam.system.medico.repository.MedicoRepository;
import edu.unam.system.horarioatencion.service.HorarioAtencionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class HorarioAtencionServiceImpl
        implements HorarioAtencionService {

    private final HorarioAtencionRepository horarioAtencionRepository;
    private final MedicoRepository medicoRepository;
    private final HorarioAtencionMapper horarioAtencionMapper;

    public HorarioAtencionServiceImpl(
            HorarioAtencionRepository horarioAtencionRepository,
            MedicoRepository medicoRepository,
            HorarioAtencionMapper horarioAtencionMapper) {

        this.horarioAtencionRepository = horarioAtencionRepository;
        this.medicoRepository = medicoRepository;
        this.horarioAtencionMapper = horarioAtencionMapper;
    }

    @Override
    public List<HorarioAtencionDTO> findByMedico(Integer idMedico) {

        medicoRepository.findById(idMedico)
                .orElseThrow(() ->
                        new ResourceNotFoundException("medico.notfound"));

        return horarioAtencionRepository
                .findByMedicoIdOrderByDiaSemanaAscHoraInicioAsc(idMedico)
                .stream()
                .map(horarioAtencionMapper::entityToDto)
                .toList();
    }

    @Override
    public HorarioAtencionDTO findById(
            Integer idMedico,
            Integer idHorario) {

        HorarioAtencion horario =
                horarioAtencionRepository.findById(idHorario)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "horario.notfound"));

        if (!horario.getMedico().getId().equals(idMedico)) {
            throw new ResourceNotFoundException(
                    "horario.notfound");
        }

        return horarioAtencionMapper.entityToDto(horario);
    }

    @Override
    public HorarioAtencionDTO save(
            Integer idMedico,
            HorarioAtencionDTO dto) {

        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "medico.notfound"));

        validarRangoHoras(dto);

        boolean existeTraslape =
                horarioAtencionRepository
                        .existsByMedicoIdAndDiaSemanaAndHoraInicioLessThanAndHoraFinGreaterThan(
                                idMedico,
                                dto.getDiaSemana(),
                                dto.getHoraFin(),
                                dto.getHoraInicio()
                        );

        if (existeTraslape) {
            throw new DuplicateResourceException(
                    "horario.overlap"
            );
        }

        HorarioAtencion horario =
                horarioAtencionMapper.dtoToEntity(dto);

        horario.setMedico(medico);

        HorarioAtencion guardado =
                horarioAtencionRepository.save(horario);

        return horarioAtencionMapper.entityToDto(guardado);
    }

    @Override
    public HorarioAtencionDTO update(
            Integer idMedico,
            Integer idHorario,
            HorarioAtencionDTO dto) {

        HorarioAtencion horario =
                horarioAtencionRepository.findById(idHorario)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "horario.notfound"));

        if (!horario.getMedico().getId().equals(idMedico)) {
            throw new ResourceNotFoundException(
                    "horario.notfound");
        }

        validarRangoHoras(dto);

        boolean existeTraslape =
                horarioAtencionRepository
                        .existsByMedicoIdAndDiaSemanaAndHoraInicioLessThanAndHoraFinGreaterThanAndIdNot(
                                idMedico,
                                dto.getDiaSemana(),
                                dto.getHoraFin(),
                                dto.getHoraInicio(),
                                idHorario
                        );

        if (existeTraslape) {
            throw new DuplicateResourceException(
                    "horario.overlap"
            );
        }

        //horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());

        if (dto.getActivo() != null) {
            horario.setActivo(dto.getActivo());
        }

        HorarioAtencion actualizado =
                horarioAtencionRepository.save(horario);

        return horarioAtencionMapper.entityToDto(actualizado);
    }

    @Override
    public void delete(
            Integer idMedico,
            Integer idHorario) {

        HorarioAtencion horario =
                horarioAtencionRepository.findById(idHorario)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "horario.notfound"));

        if (!horario.getMedico().getId().equals(idMedico)) {
            throw new ResourceNotFoundException(
                    "horario.notfound");
        }

        horarioAtencionRepository.delete(horario);
    }

    private void validarRangoHoras(HorarioAtencionDTO dto) {

        if (dto.getHoraInicio() != null
                && dto.getHoraFin() != null
                && !dto.getHoraInicio()
                .isBefore(dto.getHoraFin())) {

            throw new IllegalArgumentException(
                    "La hora de inicio debe ser menor que la hora de fin"
            );
        }
    }
}