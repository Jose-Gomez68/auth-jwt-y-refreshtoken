package com.jgz.refreshtoken.security.service;

import com.jgz.refreshtoken.security.entity.Rol;
import com.jgz.refreshtoken.security.enums.RolNombre;
import com.jgz.refreshtoken.security.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class RolService {

    @Autowired
    RolRepository rolRepository;

    public Optional<Rol> getByRolNombre (RolNombre rolNombre) {
        return rolRepository.findByRolNombre(rolNombre);
    }

}
