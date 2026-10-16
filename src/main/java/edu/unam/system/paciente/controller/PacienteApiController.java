package edu.unam.system.paciente.controller;

import edu.unam.system.paciente.model.dto.PacienteDTO;
import edu.unam.system.paciente.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@Validated
public class PacienteApiController {

    private final PacienteService pacienteService;

    // Obtener todos
    @GetMapping
    public ResponseEntity<List<PacienteDTO>> getPacientes() {

        List<PacienteDTO> pacientes = pacienteService.findAll();

        return ResponseEntity
                .ok()
                .header("X-Total-Registros",
                        String.valueOf(pacientes.size()))
                .body(pacientes);
    }

    // Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> getPaciente(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                pacienteService.findById(id)
        );
    }

    // Crear
    @PostMapping
    public ResponseEntity<PacienteDTO> createPaciente(
            @Valid @RequestBody PacienteDTO pacienteDTO) {

        PacienteDTO creado = pacienteService.save(pacienteDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/pacientes/" + creado.getIdPaciente())
                .body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> updatePaciente(
            @PathVariable Integer id,
            @Valid @RequestBody PacienteDTO pacienteDTO) {

        pacienteDTO.setIdPaciente(id);

        pacienteService.update(pacienteDTO);

        return ResponseEntity.ok(pacienteDTO);
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(
            @PathVariable Integer id) {

        pacienteService.delete(id);

        return ResponseEntity
                .noContent()
                .build();
    }

}