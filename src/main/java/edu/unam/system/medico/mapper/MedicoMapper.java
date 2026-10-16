package edu.unam.system.medico.mapper;

import edu.unam.system.medico.model.dto.MedicoDTO;
import edu.unam.system.catalogo.model.entity.Consultorio;
import edu.unam.system.catalogo.model.entity.Especialidad;
import edu.unam.system.medico.model.entity.Medico;
import org.springframework.stereotype.Component;

@Component
public class MedicoMapper {

    /**
     * Entity -> DTO
     */
    public MedicoDTO entityToDto(Medico medico){

        MedicoDTO dto = new MedicoDTO();

        dto.setId(medico.getId());
        dto.setNombreCompleto(medico.getNombreCompleto());
        dto.setNumeroIdentificacion(medico.getNumeroIdentificacion());
        dto.setTelefono(medico.getTelefono());
        dto.setEmail(medico.getEmail());
        dto.setActivo(medico.getActivo());

        if(medico.getEspecialidad()!=null){
            dto.setIdEspecialidad(
                    medico.getEspecialidad().getId());
            dto.setEspecialidad(
                    medico.getEspecialidad().getNombre());
        }

        if(medico.getConsultorio()!=null){
            dto.setIdConsultorio(
                    medico.getConsultorio().getId());
            dto.setConsultorio(
                    medico.getConsultorio().getNumero());
        }

        return dto;
    }

    /**
     * DTO -> Entity
     */
    public Medico dtoToEntity(MedicoDTO dto){

        Medico medico = new Medico();

        medico.setId(dto.getId());
        medico.setNombreCompleto(dto.getNombreCompleto());
        medico.setNumeroIdentificacion(dto.getNumeroIdentificacion());
        medico.setTelefono(dto.getTelefono());
        medico.setEmail(dto.getEmail());

        medico.setActivo(
                dto.getActivo()!=null ?
                        dto.getActivo() : true);

        if(dto.getIdEspecialidad()!=null){

            Especialidad especialidad = new Especialidad();
            especialidad.setId(dto.getIdEspecialidad());

            medico.setEspecialidad(especialidad);

        }

        if(dto.getIdConsultorio()!=null){

            Consultorio consultorio = new Consultorio();
            consultorio.setId(dto.getIdConsultorio());

            medico.setConsultorio(consultorio);

        }

        return medico;

    }

}