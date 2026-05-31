package com.alfredogarcia.proyectofinal.enlatadosmg.dto;

public class LoginRequest {
    private int id;
    private String contrasena;

    public LoginRequest() {}

    public LoginRequest(int id, String contrasena) {
        this.id = id;
        this.contrasena = contrasena;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
