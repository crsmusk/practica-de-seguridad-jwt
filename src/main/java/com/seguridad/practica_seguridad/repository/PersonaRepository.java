package com.seguridad.practica_seguridad.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seguridad.practica_seguridad.model.entities.PersonaEntity;

@Repository
public interface PersonaRepository extends JpaRepository<PersonaEntity, Long> {

    Optional<PersonaEntity> findByName(String username);

    Boolean existsByName(String username);

    Boolean existsByEmail(String email);
}
