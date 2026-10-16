package edu.unam.system.catalogo.repository;

import edu.unam.system.catalogo.model.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadRepository
        extends JpaRepository<Especialidad,Integer> {
}
