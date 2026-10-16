package edu.unam.security.service.impl;

import edu.unam.auth.model.entity.Usuario;
import edu.unam.auth.repository.UsuarioRepository;
import edu.unam.security.model.entity.RefreshToken;
import edu.unam.security.repository.RefreshTokenRepository;
import edu.unam.security.service.RefreshTokenService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl
        implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UsuarioRepository usuarioRepository;

    @Value("${security.jwt.refresh-expiration}")
    private long refreshExpiration;

    public RefreshTokenServiceImpl(
            RefreshTokenRepository refreshTokenRepository,
            UsuarioRepository usuarioRepository) {

        this.refreshTokenRepository =
                refreshTokenRepository;

        this.usuarioRepository =
                usuarioRepository;
    }


    @Override
    @Transactional
    public RefreshToken crearOReemplazar(
            String email) {

        Usuario usuario =
                usuarioRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Usuario no encontrado"
                                )
                        );


        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByUsuarioId(usuario.getId())
                        .orElseGet(RefreshToken::new);


        refreshToken.setUsuario(usuario);

        refreshToken.setToken(
                UUID.randomUUID().toString()
        );

        refreshToken.setFechaExpiracion(
                LocalDateTime.now().plus(
                        Duration.ofMillis(
                                refreshExpiration
                        )
                )
        );

        refreshToken.setRevocado(false);


        return refreshTokenRepository.save(
                refreshToken
        );
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> validar(
            String token) {

        Optional<RefreshToken> encontrado =
                refreshTokenRepository
                        .findByToken(token);


        if (encontrado.isEmpty()) {
            return Optional.empty();
        }


        RefreshToken refreshToken =
                encontrado.get();


        if (Boolean.TRUE.equals(
                refreshToken.getRevocado())) {

            return Optional.empty();
        }


        if (refreshToken
                .getFechaExpiracion()
                .isBefore(LocalDateTime.now())) {

            return Optional.empty();
        }


        return Optional.of(refreshToken);
    }


    @Override
    @Transactional
    public RefreshToken rotar(
            RefreshToken refreshToken) {

        refreshToken.setToken(
                UUID.randomUUID().toString()
        );

        refreshToken.setFechaExpiracion(
                LocalDateTime.now().plus(
                        Duration.ofMillis(
                                refreshExpiration
                        )
                )
        );

        refreshToken.setRevocado(false);


        return refreshTokenRepository.save(
                refreshToken
        );
    }


    @Override
    @Transactional
    public void revocar(String token) {

        refreshTokenRepository
                .findByToken(token)
                .ifPresent(refreshToken -> {

                    refreshToken.setRevocado(true);

                    refreshTokenRepository.save(
                            refreshToken
                    );
                });
    }
}