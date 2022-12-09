package com.jgz.refreshtoken.security.service;

import com.jgz.refreshtoken.security.entity.Usuario;
import com.jgz.refreshtoken.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Optional<Usuario> getByNameUser(String nameUser){

        return usuarioRepository.findByNameUser(nameUser);

    }

    public boolean existsByNameUser(String nameUser){

        return usuarioRepository.existsByNameUser(nameUser);

    }

    public Optional<Usuario> getByEmail(String email){

        return usuarioRepository.findByEmail(email);

    }

    public boolean existsByEmail(String email){

        return usuarioRepository.existsByEmail(email);

    }

    public void save(Usuario usuario){

        usuarioRepository.save(usuario);

    }

}
