package com.jgz.refreshtoken.security.dto;

public class MensajeCustom {
    //esta clase me sirve solo para generar mensajes
    private String mensaje;

    public MensajeCustom(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
