package edu.unam.system.cita.service;

import edu.unam.system.cita.model.dto.CitaDTO;

import java.time.LocalDate;
import java.util.List;

public interface CitaService {

    Long count();
    List<CitaDTO> findAll();
    CitaDTO findById(Integer id);
    CitaDTO save(CitaDTO dto);
    void update(CitaDTO dto);
    void delete(Integer id);

    List<CitaDTO> findByMedico(Integer idMedico);
    List<CitaDTO> findByPaciente(Integer idPaciente);
    List<CitaDTO> findByFecha(LocalDate fecha);

    // Relación N:M Paciente - Médico
    CitaDTO asociarPacienteMedico(
            Integer idPaciente,
            Integer idMedico,
            CitaDTO dto
    );

    void eliminarAsociacion(
            Integer idPaciente,
            Integer idMedico,
            Integer idCita
    );
}