package edu.unam.system.medico.service;

import edu.unam.system.medico.model.dto.MedicoDTO;
import edu.unam.system.paciente.model.dto.PacienteDTO;

import java.util.List;

public interface MedicoService {

    Long count();
    List<MedicoDTO> findAll();
    MedicoDTO findById(Integer id);
    MedicoDTO save(MedicoDTO medicoDTO);
    void update(MedicoDTO medicoDTO);
    void delete(Integer id);

    List<PacienteDTO> findPacientes(Integer idMedico);


}
