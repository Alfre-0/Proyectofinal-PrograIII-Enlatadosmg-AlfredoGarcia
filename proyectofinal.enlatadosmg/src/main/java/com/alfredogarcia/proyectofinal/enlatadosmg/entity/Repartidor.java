package com.alfredogarcia.proyectofinal.enlatadosmg.entity;

public class Repartidor {
    private String dpi; // CUI/DPI
    private String nombre;
    private String apellidos;
    private String licencia;
    private String telefono;

    public Repartidor() {}

    public Repartidor(String dpi, String nombre, String apellidos, String licencia, String telefono) {
        this.dpi = dpi;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.licencia = licencia;
        this.telefono = telefono;
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

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
