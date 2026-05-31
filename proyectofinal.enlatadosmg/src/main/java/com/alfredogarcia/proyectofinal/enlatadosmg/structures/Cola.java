package com.alfredogarcia.proyectofinal.enlatadosmg.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Cola<T> {
    private NodoSimple<T> frente;
    private NodoSimple<T> fin;
    private int tamanio;

    public Cola() {
        this.frente = null;
        this.fin = null;
        this.tamanio = 0;
    }

    public void encolar(T dato) {
        NodoSimple<T> nuevo = new NodoSimple<>(dato);
        if (estaVacia()) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.establecerSiguiente(nuevo);
            fin = nuevo;
        }
        tamanio++;
    }

    public T desencolar() {
        if (estaVacia()) {
            return null;
        }
        T dato = frente.obtenerDato();
        frente = frente.obtenerSiguiente();
        if (frente == null) {
            fin = null;
        }
        tamanio--;
        return dato;
    }

    public T obtenerFrente() {
        if (estaVacia()) {
            return null;
        }
        return frente.obtenerDato();
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int obtenerTamanio() {
        return tamanio;
    }

    public T buscar(Predicate<T> criterio) {
        NodoSimple<T> aux = frente;
        while (aux != null) {
            if (criterio.test(aux.obtenerDato())) {
                return aux.obtenerDato();
            }
            aux = aux.obtenerSiguiente();
        }
        return null;
    }

    public boolean eliminarNodo(Predicate<T> criterio) {
        if (estaVacia()) {
            return false;
        }

        if (criterio.test(frente.obtenerDato())) {
            desencolar();
            return true;
        }

        NodoSimple<T> anterior = frente;
        NodoSimple<T> actual = frente.obtenerSiguiente();

        while (actual != null) {
            if (criterio.test(actual.obtenerDato())) {
                anterior.establecerSiguiente(actual.obtenerSiguiente());
                if (actual == fin) {
                    fin = anterior;
                }
                tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.obtenerSiguiente();
        }

        return false;
    }

    public List<T> obtenerTodos() {
        List<T> lista = new ArrayList<>();
        NodoSimple<T> aux = frente;
        while (aux != null) {
            lista.add(aux.obtenerDato());
            aux = aux.obtenerSiguiente();
        }
        return lista;
    }
}
