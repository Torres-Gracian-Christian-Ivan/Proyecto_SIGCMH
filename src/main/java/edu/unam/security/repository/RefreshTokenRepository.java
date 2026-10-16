package edu.unam.security.repository;

import edu.unam.security.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Integer> {


    @EntityGraph(attributePaths = {"usuario"})
    Optional<RefreshToken> findByToken(String token);


    Optional<RefreshToken> findByUsuarioId(
            Integer idUsuario
    );
}