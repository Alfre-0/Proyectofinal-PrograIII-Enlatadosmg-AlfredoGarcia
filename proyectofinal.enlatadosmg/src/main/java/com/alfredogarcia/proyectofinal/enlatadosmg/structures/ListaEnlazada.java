package com.alfredogarcia.proyectofinal.enlatadosmg.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ListaEnlazada<T> {
    private NodoSimple<T> cabeza;
    private int tamanio;

    public ListaEnlazada() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    public void insertarInicio(T dato) {
        NodoSimple<T> nuevo = new NodoSimple<>(dato);
        if (estaVacia()) {
            cabeza = nuevo;
        } else {
            nuevo.establecerSiguiente(cabeza);
            cabeza = nuevo;
        }
        tamanio++;
    }

    public void insertarFinal(T dato) {
        NodoSimple<T> nuevo = new NodoSimple<>(dato);
        if (estaVacia()) {
            cabeza = nuevo;
        } else {
            NodoSimple<T> aux = cabeza;
            while (aux.obtenerSiguiente() != null) {
                aux = aux.obtenerSiguiente();
            }
            aux.establecerSiguiente(nuevo);
        }
        tamanio++;
    }

    public boolean eliminarNodo(Predicate<T> criterio) {
        if (estaVacia()) {
            return false;
        }

        if (criterio.test(cabeza.obtenerDato())) {
            cabeza = cabeza.obtenerSiguiente();
            tamanio--;
            return true;
        }

        NodoSimple<T> anterior = cabeza;
        NodoSimple<T> actual = cabeza.obtenerSiguiente();

        while (actual != null) {
            if (criterio.test(actual.obtenerDato())) {
                anterior.establecerSiguiente(actual.obtenerSiguiente());
                tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.obtenerSiguiente();
        }

        return false;
    }

    public T buscar(Predicate<T> criterio) {
        NodoSimple<T> aux = cabeza;
        while (aux != null) {
            if (criterio.test(aux.obtenerDato())) {
                return aux.obtenerDato();
            }
            aux = aux.obtenerSiguiente();
        }
        return null;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int obtenerTamanio() {
        return tamanio;
    }

    public NodoSimple<T> obtenerCabeza() {
        return cabeza;
    }

    public List<T> obtenerTodos() {
        List<T> lista = new ArrayList<>();
        NodoSimple<T> aux = cabeza;
        while (aux != null) {
            lista.add(aux.obtenerDato());
            aux = aux.obtenerSiguiente();
        }
        return lista;
    }
}
