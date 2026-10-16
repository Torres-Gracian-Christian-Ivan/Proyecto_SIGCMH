package edu.unam.system.catalogo.repository;

import edu.unam.system.catalogo.model.entity.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Integer> {

}
