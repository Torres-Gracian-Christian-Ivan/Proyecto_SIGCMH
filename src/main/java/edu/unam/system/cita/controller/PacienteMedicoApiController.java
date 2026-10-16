package edu.unam.system.cita.controller;

import edu.unam.system.cita.model.dto.CitaDTO;
import edu.unam.system.cita.model.dto.CitaRelacionDTO;
import edu.unam.system.medico.model.dto.MedicoDTO;
import edu.unam.system.paciente.model.dto.PacienteDTO;
import edu.unam.system.cita.service.CitaService;
import edu.unam.system.medico.service.MedicoService;
import edu.unam.system.paciente.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PacienteMedicoApiController {

    private final PacienteService pacienteService;
    private final MedicoService medicoService;
    private final CitaService citaService;

    public PacienteMedicoApiController(
            PacienteService pacienteService,
            MedicoService medicoService,
            CitaService citaService) {

        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
        this.citaService = citaService;
    }

    @GetMapping("/pacientes/{idPaciente}/medicos")
    public ResponseEntity<List<MedicoDTO>> getMedicosByPaciente(
            @PathVariable Integer idPaciente) {

        List<MedicoDTO> medicos =
                pacienteService.findMedicos(idPaciente);

        return ResponseEntity.ok()
                .header(
                        "X-Total-Registros",
                        String.valueOf(medicos.size())
                )
                .header("X-Recurso", "medicos")
                .body(medicos);
    }

    @GetMapping("/medicos/{idMedico}/pacientes")
    public ResponseEntity<List<PacienteDTO>> getPacientesByMedico(
            @PathVariable Integer idMedico) {

        List<PacienteDTO> pacientes =
                medicoService.findPacientes(idMedico);

        return ResponseEntity.ok()
                .header(
                        "X-Total-Registros",
                        String.valueOf(pacientes.size())
                )
                .header("X-Recurso", "pacientes")
                .body(pacientes);
    }


    @PostMapping(
            "/pacientes/{idPaciente}/medicos/{idMedico}/citas"
    )
    public ResponseEntity<CitaDTO> asociarPacienteMedico(
            @PathVariable Integer idPaciente,
            @PathVariable Integer idMedico,
            @Valid @RequestBody CitaRelacionDTO relacionDTO) {

        CitaDTO citaDTO = new CitaDTO();

        citaDTO.setIdPaciente(idPaciente);
        citaDTO.setIdMedico(idMedico);

        citaDTO.setIdUsuarioCreador(
                relacionDTO.getIdUsuarioCreador()
        );

        citaDTO.setFechaHora(
                relacionDTO.getFechaHora()
        );

        citaDTO.setIdEstado(
                relacionDTO.getIdEstado()
        );

        citaDTO.setMotivoConsulta(
                relacionDTO.getMotivoConsulta()
        );

        citaDTO.setObservaciones(
                relacionDTO.getObservaciones()
        );

        CitaDTO guardada =
                citaService.asociarPacienteMedico(
                        idPaciente,
                        idMedico,
                        citaDTO
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("X-Recurso", "cita")
                .body(guardada);
    }


    @DeleteMapping(
            "/pacientes/{idPaciente}/medicos/{idMedico}/citas/{idCita}"
    )
    public ResponseEntity<Void> eliminarAsociacion(
            @PathVariable Integer idPaciente,
            @PathVariable Integer idMedico,
            @PathVariable Integer idCita) {

        citaService.eliminarAsociacion(
                idPaciente,
                idMedico,
                idCita
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}