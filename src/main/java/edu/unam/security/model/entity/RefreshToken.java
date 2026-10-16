package edu.unam.security.model.entity;

import edu.unam.auth.model.entity.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(
            name = "token",
            nullable = false,
            unique = true,
            length = 100
    )
    private String token;


    @Column(
            name = "fecha_expiracion",
            nullable = false
    )
    private LocalDateTime fechaExpiracion;


    @Column(
            name = "revocado",
            nullable = false
    )
    private Boolean revocado = false;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_usuario",
            nullable = false,
            unique = true
    )
    private Usuario usuario;
}