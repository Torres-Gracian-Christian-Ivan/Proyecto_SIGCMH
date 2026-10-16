package edu.unam.system.medico.service.impl;

import edu.unam.common.exception.DuplicateResourceException;
import edu.unam.common.exception.ResourceNotFoundException;
import edu.unam.system.medico.mapper.MedicoMapper;
import edu.unam.system.paciente.mapper.PacienteMapper;
import edu.unam.system.medico.model.dto.MedicoDTO;
import edu.unam.system.paciente.model.dto.PacienteDTO;
import edu.unam.system.catalogo.model.entity.Consultorio;
import edu.unam.system.catalogo.model.entity.Especialidad;
import edu.unam.system.medico.model.entity.Medico;
import edu.unam.system.cita.repository.CitaRepository;
import edu.unam.system.catalogo.repository.ConsultorioRepository;
import edu.unam.system.catalogo.repository.EspecialidadRepository;
import edu.unam.system.medico.repository.MedicoRepository;
import edu.unam.system.medico.service.MedicoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicoServiceImpl implements MedicoService {



    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final ConsultorioRepository consultorioRepository;
    private final MedicoMapper medicoMapper;
    private final CitaRepository citaRepository;
    private final PacienteMapper pacienteMapper;

    public MedicoServiceImpl(
            MedicoRepository medicoRepository,
            EspecialidadRepository especialidadRepository,
            ConsultorioRepository consultorioRepository,
            MedicoMapper medicoMapper, CitaRepository citaRepository, PacienteMapper pacienteMapper) {

        this.medicoRepository = medicoRepository;
        this.especialidadRepository = especialidadRepository;
        this.consultorioRepository = consultorioRepository;
        this.medicoMapper = medicoMapper;
        this.citaRepository = citaRepository;
        this.pacienteMapper = pacienteMapper;
    }

    @Override
    public Long count() {return medicoRepository.count();}


    @Override
    @Transactional(readOnly = true)
    public List<MedicoDTO> findAll() {

        return medicoRepository.findAll()
                .stream()
                .map(medicoMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MedicoDTO findById(Integer id) {

        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("medico.notfound")
                );

        return medicoMapper.entityToDto(medico);
    }

    @Override
    public MedicoDTO save(MedicoDTO medicoDTO) {

        // Validar número de identificación duplicado
        if (medicoDTO.getId() == null &&
                medicoRepository.existsByNumeroIdentificacion(
                        medicoDTO.getNumeroIdentificacion())) {

            throw new DuplicateResourceException("medico.duplicate");
        }

        // Validar
        Especialidad especialidad = especialidadRepository.findById(
                        medicoDTO.getIdEspecialidad())
                .orElseThrow(() ->
                        new ResourceNotFoundException("especialidad.notfound"));

        Consultorio consultorio = consultorioRepository.findById(
                        medicoDTO.getIdConsultorio())
                .orElseThrow(() ->
                        new ResourceNotFoundException("consultorio.notfound"));

        Medico medico = medicoMapper.dtoToEntity(medicoDTO);


        medico.setEspecialidad(especialidad);
        medico.setConsultorio(consultorio);

        Medico guardado = medicoRepository.save(medico);

        return medicoMapper.entityToDto(guardado);
    }


    @Override
    public void update(MedicoDTO dto) {

        Medico medico = medicoRepository.findById(dto.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("medico.notfound"));

        // Validar que el número de identificación no pertenezca a otro médico
        if (medicoRepository.existsByNumeroIdentificacionAndIdNot(
                dto.getNumeroIdentificacion(),
                dto.getId())) {

            throw new DuplicateResourceException("medico.duplicate");
        }

        medico.setNombreCompleto(dto.getNombreCompleto());
        medico.setNumeroIdentificacion(dto.getNumeroIdentificacion());
        medico.setTelefono(dto.getTelefono());
        medico.setEmail(dto.getEmail());
        medico.setActivo(dto.getActivo());

        Especialidad especialidad = especialidadRepository.findById(dto.getIdEspecialidad())
                .orElseThrow(() ->
                        new ResourceNotFoundException("especialidad.notfound"));

        Consultorio consultorio = consultorioRepository.findById(dto.getIdConsultorio())
                .orElseThrow(() ->
                        new ResourceNotFoundException("consultorio.notfound"));

        medico.setEspecialidad(especialidad);
        medico.setConsultorio(consultorio);

        medicoRepository.save(medico);
    }

    @Override
    public void delete(Integer id) {
        if(!medicoRepository.existsById(id)){
            throw new ResourceNotFoundException(
                    "paciente.notfound");
        }
        medicoRepository.deleteById(id);
    }

    @Override
    public List<PacienteDTO> findPacientes(Integer idMedico) {

        // comprobamos que el médico exista
        medicoRepository.findById(idMedico)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "medico.notfound"
                        ));

        return citaRepository
                .findPacientesByMedicoId(idMedico)
                .stream()
                .map(pacienteMapper::entityToDto)
                .toList();
    }
}
