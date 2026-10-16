package edu.unam.security.service;

import edu.unam.security.model.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken crearOReemplazar(String email);
    Optional<RefreshToken> validar(String token);
    RefreshToken rotar(RefreshToken refreshToken);
    void revocar(String token);
}