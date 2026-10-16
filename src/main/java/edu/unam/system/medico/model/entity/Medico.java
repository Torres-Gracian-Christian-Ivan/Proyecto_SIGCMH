package edu.unam.system.medico.model.entity;

import edu.unam.system.catalogo.model.entity.Consultorio;
import edu.unam.system.catalogo.model.entity.Especialidad;
import edu.unam.system.horarioatencion.model.entity.HorarioAtencion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "medico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(name = "numero_identificacion", nullable = false)
    private String numeroIdentificacion;

    private String telefono;

    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_especialidad", nullable = false)
    private Especialidad especialidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consultorio", nullable = false)
    private Consultorio consultorio;

    @Column(nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "medico", fetch = FetchType.LAZY)
    private List<HorarioAtencion> horarios;

}