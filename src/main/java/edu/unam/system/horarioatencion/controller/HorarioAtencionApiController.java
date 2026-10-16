package edu.unam.system.horarioatencion.controller;

import edu.unam.system.horarioatencion.model.dto.HorarioAtencionDTO;
import edu.unam.system.horarioatencion.service.HorarioAtencionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos/{idMedico}/horarios")
public class HorarioAtencionApiController {

    private final HorarioAtencionService horarioAtencionService;

    public HorarioAtencionApiController(
            HorarioAtencionService horarioAtencionService) {
        this.horarioAtencionService = horarioAtencionService;
    }


    // Obtener todos los horarios de un médico
    @GetMapping
    public ResponseEntity<List<HorarioAtencionDTO>> getHorarios(
            @PathVariable Integer idMedico) {

        List<HorarioAtencionDTO> horarios =
                horarioAtencionService.findByMedico(idMedico);

        return ResponseEntity.ok()
                .header(
                        "X-Total-Registros",
                        String.valueOf(horarios.size())
                )
                .header("X-Recurso", "horarios")
                .body(horarios);
    }


    // Obtener un horario específico perteneciente a un médico
    @GetMapping("/{idHorario}")
    public ResponseEntity<HorarioAtencionDTO> getHorario(
            @PathVariable Integer idMedico,
            @PathVariable Integer idHorario) {

        HorarioAtencionDTO horario =
                horarioAtencionService.findById(
                        idMedico,
                        idHorario
                );

        return ResponseEntity.ok(horario);
    }


    // Agregar un nuevo horario a un médico
    @PostMapping
    public ResponseEntity<HorarioAtencionDTO> createHorario(
            @PathVariable Integer idMedico,
            @Valid @RequestBody HorarioAtencionDTO dto) {

        HorarioAtencionDTO guardado =
                horarioAtencionService.save(
                        idMedico,
                        dto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("X-Recurso", "horario")
                .body(guardado);
    }


    // Actualizar un horario perteneciente a un médico
    @PutMapping("/{idHorario}")
    public ResponseEntity<HorarioAtencionDTO> updateHorario(
            @PathVariable Integer idMedico,
            @PathVariable Integer idHorario,
            @Valid @RequestBody HorarioAtencionDTO dto) {

        HorarioAtencionDTO actualizado =
                horarioAtencionService.update(
                        idMedico,
                        idHorario,
                        dto
                );

        return ResponseEntity.ok(actualizado);
    }


    // Eliminar un horario perteneciente a un médico
    @DeleteMapping("/{idHorario}")
    public ResponseEntity<Void> deleteHorario(
            @PathVariable Integer idMedico,
            @PathVariable Integer idHorario) {

        horarioAtencionService.delete(
                idMedico,
                idHorario
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}