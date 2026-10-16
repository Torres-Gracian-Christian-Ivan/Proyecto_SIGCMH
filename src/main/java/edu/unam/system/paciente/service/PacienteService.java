package edu.unam.system.paciente.service;

import edu.unam.system.medico.model.dto.MedicoDTO;
import edu.unam.system.paciente.model.dto.PacienteDTO;

import java.util.List;

public interface PacienteService {

    Long count();
    List<PacienteDTO> findAll();
    PacienteDTO findById(Integer id);
    //void save(PacienteDTO paciente);
    PacienteDTO save(PacienteDTO dto);
    void update(PacienteDTO paciente);
    void delete(Integer id);



    List<MedicoDTO> findMedicos(Integer idPaciente);

}