package edu.unam.system.cita.service.impl;

import edu.unam.auth.model.entity.Usuario;
import edu.unam.auth.repository.UsuarioRepository;
import edu.unam.common.exception.DuplicateResourceException;
import edu.unam.common.exception.ResourceNotFoundException;
import edu.unam.system.cita.mapper.CitaMapper;
import edu.unam.system.cita.model.dto.CitaDTO;
import edu.unam.system.cita.model.entity.*;
import edu.unam.system.cita.repository.*;
import edu.unam.system.cita.service.CitaService;
import edu.unam.system.estadocita.model.entity.EstadoCita;
import edu.unam.system.estadocita.repository.EstadoCitaRepository;
import edu.unam.system.medico.model.entity.Medico;
import edu.unam.system.medico.repository.MedicoRepository;
import edu.unam.system.paciente.model.entity.Paciente;
import edu.unam.system.paciente.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoCitaRepository estadoCitaRepository;

    private final CitaMapper citaConverter;

    @Override
    public Long count() {return citaRepository.count();}

    @Override
    @Transactional(readOnly = true)
    public List<CitaDTO> findAll() {

        return citaRepository.findAll()
                .stream()
                .map(citaConverter::entityToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CitaDTO findById(Integer id) {

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "cita.notfound"
                        )
                );

        return citaConverter.entityToDto(cita);
    }

    @Override
    public CitaDTO save(CitaDTO dto) {

        Paciente paciente = pacienteRepository.findById(dto.getIdPaciente())
                .orElseThrow(() ->
                        new ResourceNotFoundException("paciente.notfound"));

        Medico medico = medicoRepository.findById(dto.getIdMedico())
                .orElseThrow(() ->
                        new ResourceNotFoundException("medico.notfound"));

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuarioCreador())
                .orElseThrow(() ->
                        new ResourceNotFoundException("usuario.notfound"));

        EstadoCita estado = estadoCitaRepository.findById(dto.getIdEstado())
                .orElseThrow(() ->
                        new ResourceNotFoundException("estadocita.notfound"));

        // Validar que el médico no tenga otra cita en ese horario
        if (citaRepository.existsByMedicoIdAndFechaHora(
                dto.getIdMedico(),
                dto.getFechaHora())) {

            throw new DuplicateResourceException("cita.medico.ocupado");
        }

        // Validar que el paciente no tenga otra cita en ese horario
        if (citaRepository.existsByPacienteIdPacienteAndFechaHora(
                dto.getIdPaciente(),
                dto.getFechaHora())) {

            throw new DuplicateResourceException("cita.paciente.ocupado");
        }

        Cita cita = citaConverter.dtoToEntity(dto);

        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setUsuarioCreador(usuario);
        cita.setEstado(estado);

        cita.setFechaCreacion(LocalDateTime.now());
        cita.setFechaActualizacion(LocalDateTime.now());

        Cita guardada = citaRepository.save(cita);

        return citaConverter.entityToDto(guardada);
    }

    @Override
    public void update(CitaDTO dto) {

        Cita cita = citaRepository.findById(dto.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("cita.notfound"));

        Paciente paciente = pacienteRepository.findById(dto.getIdPaciente())
                .orElseThrow(() ->
                        new ResourceNotFoundException("paciente.notfound"));

        Medico medico = medicoRepository.findById(dto.getIdMedico())
                .orElseThrow(() ->
                        new ResourceNotFoundException("medico.notfound"));

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuarioCreador())
                .orElseThrow(() ->
                        new ResourceNotFoundException("usuario.notfound"));

        EstadoCita estado = estadoCitaRepository.findById(dto.getIdEstado())
                .orElseThrow(() ->
                        new ResourceNotFoundException("estadocita.notfound"));

        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setUsuarioCreador(usuario);
        cita.setEstado(estado);

        cita.setFechaHora(dto.getFechaHora());
        cita.setMotivoConsulta(dto.getMotivoConsulta());
        cita.setObservaciones(dto.getObservaciones());

        citaRepository.save(cita);
    }

    @Override
    public void delete(Integer id) {

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("cita.notfound"));

        citaRepository.delete(cita);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaDTO> findByMedico(Integer idMedico) {

        return citaRepository.findByMedicoId(idMedico)
                .stream()
                .map(citaConverter::entityToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaDTO> findByPaciente(Integer idPaciente) {

        return citaRepository.findByPacienteIdPaciente(idPaciente)
                .stream()
                .map(citaConverter::entityToDto)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public List<CitaDTO> findByFecha(LocalDate fecha) {

        return citaRepository.findByFechaHoraBetween(
                        fecha.atStartOfDay(),
                        fecha.plusDays(1).atStartOfDay()
                )
                .stream()
                .map(citaConverter::entityToDto)
                .toList();
    }

    @Override
    public CitaDTO asociarPacienteMedico(
            Integer idPaciente,
            Integer idMedico,
            CitaDTO dto) {

        dto.setIdPaciente(idPaciente);
        dto.setIdMedico(idMedico);

        return save(dto);
    }

    @Override
    public void eliminarAsociacion(
            Integer idPaciente,
            Integer idMedico,
            Integer idCita) {

        // si paciente existe
        pacienteRepository.findById(idPaciente)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "paciente.notfound"));

        // si médico existe
        medicoRepository.findById(idMedico)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "medico.notfound"));

        // busca la cita que representa la asociación
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "cita.notfound"));

        // si que la cita realmente pertenezca al paciente y médico indicados
        if (!cita.getPaciente().getIdPaciente().equals(idPaciente)
                || !cita.getMedico().getId().equals(idMedico)) {

            throw new ResourceNotFoundException(
                    "cita.relacion.notfound");
        }

        citaRepository.delete(cita);
    }

}