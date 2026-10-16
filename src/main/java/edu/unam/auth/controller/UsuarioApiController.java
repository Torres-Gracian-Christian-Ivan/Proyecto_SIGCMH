package edu.unam.auth.controller;

import edu.unam.auth.model.dto.UsuarioDTO;
import edu.unam.auth.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioApiController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getUsuarios() {

        List<UsuarioDTO> usuarios = usuarioService.findAll();

        return ResponseEntity
                .ok()
                .header("X-Total-Registros",
                        String.valueOf(usuarios.size()))
                .header("X-Recurso", "usuarios")
                .body(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuario(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                usuarioService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> createUsuario(
            @Valid @RequestBody UsuarioDTO usuarioDTO) {

        UsuarioDTO guardado =
                usuarioService.save(usuarioDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("X-Recurso", "usuario")
                .body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioDTO usuarioDTO) {

        usuarioDTO.setId(id);

        usuarioService.update(usuarioDTO);

        return ResponseEntity.ok(usuarioDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(
            @PathVariable Integer id) {

        usuarioService.delete(id);

        return ResponseEntity.noContent().build();
    }

}