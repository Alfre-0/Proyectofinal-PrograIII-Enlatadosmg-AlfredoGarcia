package com.alfredogarcia.proyectofinal.enlatadosmg.structures;

import java.util.ArrayList;
import java.util.List;

public class Pila<T> {
    private NodoSimple<T> cima;
    private int tamanio;

    public Pila() {
        this.cima = null;
        this.tamanio = 0;
    }

    public void apilar(T dato) {
        NodoSimple<T> nuevo = new NodoSimple<>(dato);
        if (estaVacia()) {
            cima = nuevo;
        } else {
            nuevo.establecerSiguiente(cima);
            cima = nuevo;
        }
        tamanio++;
    }

    public T desapilar() {
        if (estaVacia()) {
            return null;
        }
        T dato = cima.obtenerDato();
        cima = cima.obtenerSiguiente();
        tamanio--;
        return dato;
    }

    public T obtenerCima() {
        if (estaVacia()) {
            return null;
        }
        return cima.obtenerDato();
    }

    public boolean estaVacia() {
        return cima == null;
    }

    public int obtenerTamanio() {
        return tamanio;
    }

    public List<T> obtenerTodos() {
        List<T> lista = new ArrayList<>();
        NodoSimple<T> aux = cima;
        while (aux != null) {
            lista.add(aux.obtenerDato());
            aux = aux.obtenerSiguiente();
        }
        return lista;
    }
}
