package edu.unam.system.catalogo.repository;

import edu.unam.system.catalogo.model.entity.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultorioRepository
        extends JpaRepository<Consultorio,Integer> {
}