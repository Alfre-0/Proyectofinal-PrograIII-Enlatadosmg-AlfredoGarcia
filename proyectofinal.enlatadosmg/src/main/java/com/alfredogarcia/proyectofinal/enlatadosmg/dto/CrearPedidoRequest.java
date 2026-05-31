package com.alfredogarcia.proyectofinal.enlatadosmg.dto;

public class CrearPedidoRequest {
    private String dpiCliente;
    private String departamentoOrigen;
    private String departamentoDestino;
    private int cantidadCajas;

    public CrearPedidoRequest() {}

    public CrearPedidoRequest(String dpiCliente, String departamentoOrigen, String departamentoDestino, int cantidadCajas) {
        this.dpiCliente = dpiCliente;
        this.departamentoOrigen = departamentoOrigen;
        this.departamentoDestino = departamentoDestino;
        this.cantidadCajas = cantidadCajas;
    }

    public String getDpiCliente() {
        return dpiCliente;
    }

    public void setDpiCliente(String dpiCliente) {
        this.dpiCliente = dpiCliente;
    }

    public String getDepartamentoOrigen() {
        return departamentoOrigen;
    }

    public void setDepartamentoOrigen(String departamentoOrigen) {
        this.departamentoOrigen = departamentoOrigen;
    }

    public String getDepartamentoDestino() {
        return departamentoDestino;
    }

    public void setDepartamentoDestino(String departamentoDestino) {
        this.departamentoDestino = departamentoDestino;
    }

    public int getCantidadCajas() {
        return cantidadCajas;
    }

    public void setCantidadCajas(int cantidadCajas) {
        this.cantidadCajas = cantidadCajas;
    }
}
