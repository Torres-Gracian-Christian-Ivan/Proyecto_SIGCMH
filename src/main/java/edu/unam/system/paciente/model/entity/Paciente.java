package edu.unam.system.paciente.model.entity;


import edu.unam.system.catalogo.model.entity.Genero;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "paciente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Integer idPaciente;

    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;

    @Column(name = "numero_identificacion", nullable = false, length = 20, unique = true)
    private String numeroIdentificacion;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_genero", nullable = false)
    private Genero genero;

    @Column(length = 15)
    private String telefono;

    @Column(length = 100)
    private String email;

    @Column(length = 255)
    private String direccion;

    @Column(nullable = false)
    private Boolean activo;

}
