package com.jgz.refreshtoken.security.repository;

import com.jgz.refreshtoken.security.entity.Rol;
import com.jgz.refreshtoken.security.enums.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol,Long> {

    Optional<Rol> findByRolNombre(RolNombre rolNombre);

}
