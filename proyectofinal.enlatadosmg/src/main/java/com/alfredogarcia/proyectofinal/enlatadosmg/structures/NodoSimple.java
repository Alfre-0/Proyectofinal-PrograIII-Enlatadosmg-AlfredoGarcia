package com.alfredogarcia.proyectofinal.enlatadosmg.structures;

public class NodoSimple<T> {
    private T dato;
    private NodoSimple<T> siguiente;

    public NodoSimple(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public T obtenerDato() {
        return dato;
    }

    public void establecerDato(T dato) {
        this.dato = dato;
    }

    public NodoSimple<T> obtenerSiguiente() {
        return siguiente;
    }

    public void establecerSiguiente(NodoSimple<T> siguiente) {
        this.siguiente = siguiente;
    }
}
