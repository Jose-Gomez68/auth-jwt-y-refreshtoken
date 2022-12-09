package com.jgz.refreshtoken.security.repository;

import com.jgz.refreshtoken.security.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNameUser(String nameUser);
    boolean existsByNameUser(String nameUser);

    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String nameUser);

}
