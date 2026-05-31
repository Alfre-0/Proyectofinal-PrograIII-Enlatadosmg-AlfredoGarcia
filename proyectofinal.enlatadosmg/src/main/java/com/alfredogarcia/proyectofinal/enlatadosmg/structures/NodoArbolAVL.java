package com.alfredogarcia.proyectofinal.enlatadosmg.structures;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Cliente;

public class NodoArbolAVL {
    private Cliente cliente;
    private NodoArbolAVL izquierdo;
    private NodoArbolAVL derecho;
    private int altura;

    public NodoArbolAVL(Cliente cliente) {
        this.cliente = cliente;
        this.izquierdo = null;
        this.derecho = null;
        this.altura = 1;
    }

    public Cliente obtenerCliente() {
        return cliente;
    }

    public void establecerCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public NodoArbolAVL obtenerIzquierdo() {
        return izquierdo;
    }

    public void establecerIzquierdo(NodoArbolAVL izquierdo) {
        this.izquierdo = izquierdo;
    }

    public NodoArbolAVL obtenerDerecho() {
        return derecho;
    }

    public void establecerDerecho(NodoArbolAVL derecho) {
        this.derecho = derecho;
    }

    public int obtenerAltura() {
        return altura;
    }

    public void establecerAltura(int altura) {
        this.altura = altura;
    }
}
