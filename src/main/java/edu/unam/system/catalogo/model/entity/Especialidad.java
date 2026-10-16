package edu.unam.system.catalogo.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "especialidad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String descripcion;
    public Especialidad(Integer id) {
        this.id = id;
    }

}
