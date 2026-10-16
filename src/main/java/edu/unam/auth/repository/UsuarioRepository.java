package edu.unam.auth.repository;

import edu.unam.auth.model.entity.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository
        extends JpaRepository<Usuario, Integer> {

    @EntityGraph(attributePaths = {"perfil"})
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(
            String email,
            Integer id
    );

    @EntityGraph(attributePaths = {"perfil"})
    @Override
    List<Usuario> findAll();

    @EntityGraph(attributePaths = {"perfil"})
    @Override
    Optional<Usuario> findById(Integer id);
}