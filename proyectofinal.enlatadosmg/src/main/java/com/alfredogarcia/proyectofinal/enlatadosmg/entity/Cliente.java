package com.alfredogarcia.proyectofinal.enlatadosmg.entity;

public class Cliente {
    private String dpi; // Usado como CUI
    private String nombre;
    private String apellidos;
    private String telefono;
    private String direccion;

    public Cliente() {}

    public Cliente(String dpi, String nombre, String apellidos, String telefono, String direccion) {
        this.dpi = dpi;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
