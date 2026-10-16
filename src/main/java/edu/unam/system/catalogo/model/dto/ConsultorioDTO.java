package edu.unam.system.catalogo.model.dto;

import lombok.Data;

@Data
public class ConsultorioDTO {
    private Integer id;
    private String numero;
    private String ubicacion;
    private Boolean activo;

}