package edu.unam.auth.repository;

import edu.unam.auth.model.entity.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
    List<Perfil> findByActivoTrue();
    boolean existsByNombre(String nombre);


}