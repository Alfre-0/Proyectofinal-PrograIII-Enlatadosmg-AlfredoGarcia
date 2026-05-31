package com.alfredogarcia.proyectofinal.enlatadosmg.entity;

import com.alfredogarcia.proyectofinal.enlatadosmg.structures.ListaCajasPedido;

public class Pedido {
    private int numeroPedido;
    private String departamentoOrigen;
    private String departamentoDestino;
    private String fechaHoraInicio;
    private Cliente cliente;
    private Repartidor repartidor;
    private Vehiculo vehiculo;
    private ListaCajasPedido cajasProducto;
    private int numeroCajas;
    private EstadoPedido estado;

    public Pedido() {}

    public Pedido(int numeroPedido, String departamentoOrigen, String departamentoDestino, 
                  String fechaHoraInicio, Cliente cliente, Repartidor repartidor, 
                  Vehiculo vehiculo, ListaCajasPedido cajasProducto, int numeroCajas, 
                  EstadoPedido estado) {
        this.numeroPedido = numeroPedido;
        this.departamentoOrigen = departamentoOrigen;
        this.departamentoDestino = departamentoDestino;
        this.fechaHoraInicio = fechaHoraInicio;
        this.cliente = cliente;
        this.repartidor = repartidor;
        this.vehiculo = vehiculo;
        this.cajasProducto = cajasProducto;
        this.numeroCajas = numeroCajas;
        this.estado = estado;
    }

    public int getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
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

    public String getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(String fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    public ListaCajasPedido getCajasProducto() {
        return cajasProducto;
    }

    public void setCajasProducto(ListaCajasPedido cajasProducto) {
        this.cajasProducto = cajasProducto;
    }

    public java.util.List<Caja> getCajas() {
        if (cajasProducto == null) {
            return java.util.Collections.emptyList();
        }
        return cajasProducto.obtenerTodas();
    }

    public int getNumeroCajas() {
        return numeroCajas;
    }

    public void setNumeroCajas(int numeroCajas) {
        this.numeroCajas = numeroCajas;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }
}
