package com.jgz.refreshtoken.security.dto;

import com.sun.istack.NotNull;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class NuevoUsuario {
    //para dar de alta a usuarios

    @NotNull
    private String name;
    @NotNull
    private String nameUser;
    @NotNull
    private Date creacionDate;
    @NotNull
    private String email;
    @NotNull
    private String password;
    private Set<String> roles = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public Date getCreacionDate() {
        return creacionDate;
    }

    public void setCreacionDate(Date creacionDate) {
        this.creacionDate = creacionDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
