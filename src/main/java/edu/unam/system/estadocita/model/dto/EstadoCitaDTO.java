package edu.unam.system.estadocita.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EstadoCitaDTO {

    private Integer id;

    @NotBlank(message = "{estadocita.nombre.required}")
    @Size(max = 30, message = "{estadocita.nombre.size}")
    private String nombre;

    private Boolean activo;

}