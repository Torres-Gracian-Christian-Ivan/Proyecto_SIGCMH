package edu.unam.system.catalogo.model.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "genero")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Genero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_genero")
    private Integer idGenero;

    private String nombre;
    private Boolean activo;

}
