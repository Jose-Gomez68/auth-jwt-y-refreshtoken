package com.jgz.refreshtoken.security.service;

import com.jgz.refreshtoken.security.entity.Usuario;
import com.jgz.refreshtoken.security.entity.UsuarioPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //aqui mando el email por que quiero que autentique por email no por nameUser
        System.out.println("AQUI "+ username);
        Usuario usuario = usuarioService.getByEmail(username).get();
        System.out.println("usuario"+usuario.getEmail()+" "+usuario.getPassword());
        return UsuarioPrincipal.build(usuario);
    }

}
