package com.jgz.refreshtoken.security.dto;

import com.jgz.refreshtoken.security.entity.Rol;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtDto {

    //esta clase nos va devolver la informacion dle usuario y el token despues de que el login sea correcto

    private String token;
    private String bearer = "Bearer";
    private String email;
    //private String nameUser;

    private Long id;
    private Collection<? extends GrantedAuthority> authorities;

    // ver si aplico los cambios: https://www.youtube.com/watch?v=IC7qBKW92ic&list=PL4bT56Uw3S4z9rtwwGvuk1Mjhu5sdLSwX&index=15&ab_channel=LuigiCode
    /*PROBLAMENTE LO ARE COMO EL VIDEO ASI CUANDO DE BORRAR TODAS LAS PROPIEDADES MENOS TOKEN
    * PARA ENVIARLO EL REFRESH TOKEN O BUSCO LA MANERA DE MANDAR DENUEVO TODA ESA INFO*/

    public JwtDto(String token, String email,Long id, Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.email = email;
        //this.nameUser = nameUser;
        this.id = id;
        this.authorities = authorities;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBearer() {
        return bearer;
    }

    public void setBearer(String bearer) {
        this.bearer = bearer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }*/

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }


}
