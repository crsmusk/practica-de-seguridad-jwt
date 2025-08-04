package com.seguridad.practica_seguridad.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.seguridad.practica_seguridad.model.entities.ERole;
import com.seguridad.practica_seguridad.model.entities.RoleEntity;

@Repository
public interface RolRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(ERole name);

    Boolean existsByName(ERole name);

    @Query("SELECT r FROM RoleEntity r WHERE CAST(r.name AS string) IN :roles")
    List<RoleEntity> findRoleEntitiesByERoleIn(List<String> roles);

}
