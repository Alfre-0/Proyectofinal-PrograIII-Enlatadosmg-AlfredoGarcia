package com.alfredogarcia.proyectofinal.enlatadosmg.entity;

public class Caja {
    private int correlativo;
    private String fechaIngreso;

    public Caja() {}

    public Caja(int correlativo, String fechaIngreso) {
        this.correlativo = correlativo;
        this.fechaIngreso = fechaIngreso;
    }

    public int getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(int correlativo) {
        this.correlativo = correlativo;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
}
