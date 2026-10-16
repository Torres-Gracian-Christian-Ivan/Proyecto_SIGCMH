package edu.unam.system.paciente.service.impl;

import edu.unam.common.exception.DuplicateResourceException;
import edu.unam.common.exception.ResourceNotFoundException;
import edu.unam.system.cita.mapper.CitaMapper;
import edu.unam.system.medico.mapper.MedicoMapper;
import edu.unam.system.paciente.mapper.PacienteMapper;
import edu.unam.system.medico.model.dto.MedicoDTO;
import edu.unam.system.paciente.model.dto.PacienteDTO;
import edu.unam.system.catalogo.model.entity.Genero;
import edu.unam.system.paciente.model.entity.Paciente;
import edu.unam.system.cita.repository.CitaRepository;
import edu.unam.system.catalogo.repository.GeneroRepository;
import edu.unam.system.paciente.repository.PacienteRepository;
import edu.unam.system.paciente.service.PacienteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final GeneroRepository generoRepository;
    private final PacienteMapper pacienteMapper;
    private final CitaRepository citaRepository;
    private final MedicoMapper medicoMapper;

    public PacienteServiceImpl(PacienteRepository pacienteRepository,
                               GeneroRepository generoRepository,
                               PacienteMapper pacienteMapper, CitaMapper pacienteConverter, CitaRepository citaRepository, MedicoMapper medicoMapper) {
        this.pacienteRepository = pacienteRepository;
        this.generoRepository = generoRepository;
        this.pacienteMapper = pacienteMapper;
        this.citaRepository = citaRepository;
        this.medicoMapper = medicoMapper;
    }

    @Override
    public Long count() {
        return pacienteRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteDTO> findAll() {

        return pacienteRepository.findAll()
                .stream()
                .map(pacienteMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteDTO findById(Integer id) {

        Paciente paciente =
                pacienteRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "paciente.notfound"
                                )
                        );

        return pacienteMapper.entityToDto(paciente);
    }

    @Override
    public PacienteDTO save(PacienteDTO pacienteDTO) {

        if (pacienteDTO.getIdPaciente() == null &&
                pacienteRepository.existsByNumeroIdentificacion(
                        pacienteDTO.getNumeroIdentificacion())) {

            throw new DuplicateResourceException("paciente.duplicate");
        }

        Paciente paciente = pacienteMapper.dtoToEntity(pacienteDTO);

        Paciente guardado = pacienteRepository.save(paciente);

        return pacienteMapper.entityToDto(guardado);
    }

    @Override
    public void update(PacienteDTO dto) {

        Paciente paciente = pacienteRepository.findById(dto.getIdPaciente())
                .orElseThrow(() ->
                        new ResourceNotFoundException("paciente.notfound"));

        // Validar que el número de identificación no pertenezca a otro paciente
        if (pacienteRepository.existsByNumeroIdentificacionAndIdPacienteNot(
                dto.getNumeroIdentificacion(),
                dto.getIdPaciente())) {

            throw new DuplicateResourceException("paciente.duplicate");
        }

        Genero genero = generoRepository.findById(dto.getIdGenero())
                .orElseThrow(() ->
                        new ResourceNotFoundException("genero.notfound"));

        paciente.setNombreCompleto(dto.getNombreCompleto());
        paciente.setNumeroIdentificacion(dto.getNumeroIdentificacion());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setGenero(genero);
        paciente.setTelefono(dto.getTelefono());
        paciente.setEmail(dto.getEmail());
        paciente.setDireccion(dto.getDireccion());
        paciente.setActivo(dto.getActivo());

        pacienteRepository.save(paciente);
    }

    @Override
    public void delete(Integer id) {

        if (!pacienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("paciente.notfound");
        }

        pacienteRepository.deleteById(id);
    }

    @Override
    public List<MedicoDTO> findMedicos(Integer idPaciente) {
        //comprobamos que el paciente exista
        pacienteRepository.findById(idPaciente)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "paciente.notfound"
                        ));

        return citaRepository
                .findMedicosByPacienteId(idPaciente)
                .stream()
                .map(medicoMapper::entityToDto)
                .toList();
    }

}