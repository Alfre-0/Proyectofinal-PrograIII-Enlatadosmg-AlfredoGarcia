package com.alfredogarcia.proyectofinal.enlatadosmg.structures;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Caja;
import java.util.ArrayList;
import java.util.List;

public class ListaCajasPedido {
    private NodoSimple<Caja> cabeza;
    private int tamanio;

    public ListaCajasPedido() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    public void agregar(Caja caja) {
        NodoSimple<Caja> nuevo = new NodoSimple<>(caja);
        if (estaVacia()) {
            cabeza = nuevo;
        } else {
            NodoSimple<Caja> aux = cabeza;
            while (aux.obtenerSiguiente() != null) {
                aux = aux.obtenerSiguiente();
            }
            aux.establecerSiguiente(nuevo);
        }
        tamanio++;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int obtenerTamanio() {
        return tamanio;
    }

    public NodoSimple<Caja> obtenerCabeza() {
        return cabeza;
    }

    public List<Caja> obtenerTodas() {
        List<Caja> lista = new ArrayList<>();
        NodoSimple<Caja> aux = cabeza;
        while (aux != null) {
            lista.add(aux.obtenerDato());
            aux = aux.obtenerSiguiente();
        }
        return lista;
    }
}
