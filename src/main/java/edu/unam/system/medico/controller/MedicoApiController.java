package edu.unam.system.medico.controller;

import edu.unam.system.medico.model.dto.MedicoDTO;
import edu.unam.system.medico.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoApiController {

    private final MedicoService medicoService;
    
    // Obtener todos
    @GetMapping
    public ResponseEntity<List<MedicoDTO>> getMedicos() {

        List<MedicoDTO> medicos = medicoService.findAll();

        return ResponseEntity
                .ok()
                .header("X-Total-Registros",
                        String.valueOf(medicos.size()))
                .header("X-Recurso", "medicos")
                .body(medicos);
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> getMedico(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                medicoService.findById(id)
        );
    }

    // Crear
    @PostMapping
    public ResponseEntity<MedicoDTO> createMedico(
            @Valid @RequestBody MedicoDTO medicoDTO) {

        MedicoDTO guardado = medicoService.save(medicoDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("X-Recurso", "medico")
                .body(guardado);
    }

    // Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<MedicoDTO> updateMedico(
            @PathVariable Integer id,
            @Valid @RequestBody MedicoDTO medicoDTO) {

        medicoDTO.setId(id);

        medicoService.update(medicoDTO);

        return ResponseEntity.ok(medicoDTO);
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedico(
            @PathVariable Integer id) {

        medicoService.delete(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}