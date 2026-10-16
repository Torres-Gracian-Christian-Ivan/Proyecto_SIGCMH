package edu.unam.system.cita.controller;

import edu.unam.system.cita.model.dto.CitaDTO;
import edu.unam.system.cita.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@Validated
public class CitaApiController {

    private final CitaService citaService;

    // Obtener todas
    @GetMapping
    public ResponseEntity<List<CitaDTO>> getCitas() {

        List<CitaDTO> citas = citaService.findAll();

        return ResponseEntity
                .ok()
                .header("X-Total-Registros",
                        String.valueOf(citas.size()))
                .header("X-Recurso", "citas")
                .body(citas);
    }

    // Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<CitaDTO> getCita(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                citaService.findById(id));
    }

    // Crear
    @PostMapping
    public ResponseEntity<CitaDTO> createCita(
            @Valid @RequestBody CitaDTO dto) {

        CitaDTO creada = citaService.save(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location",
                        "/api/citas/" + creada.getId())
                .header("X-Recurso", "cita")
                .body(creada);
    }

    // Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<CitaDTO> updateCita(
            @PathVariable Integer id,
            @Valid @RequestBody CitaDTO dto) {

        dto.setId(id);

        citaService.update(dto);

        return ResponseEntity.ok(dto);
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(
            @PathVariable Integer id) {

        citaService.delete(id);

        return ResponseEntity.noContent().build();
    }

    // Buscar por médico
    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<List<CitaDTO>> getByMedico(
            @PathVariable Integer idMedico) {

        return ResponseEntity.ok(
                citaService.findByMedico(idMedico));
    }

    // Buscar por paciente
    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<CitaDTO>> getByPaciente(
            @PathVariable Integer idPaciente) {

        return ResponseEntity.ok(
                citaService.findByPaciente(idPaciente));
    }

    // Buscar por fecha
    @GetMapping("/fecha")
    public ResponseEntity<List<CitaDTO>> getByFecha(

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {

        return ResponseEntity.ok(
                citaService.findByFecha(fecha));
    }

}