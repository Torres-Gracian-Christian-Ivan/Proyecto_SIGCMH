package edu.unam.system.catalogo.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "consultorio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Consultorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String numero;
    private String ubicacion;
    private Boolean activo;
    public Consultorio(Integer id) {
        this.id = id;
    }

}