package com.alfredogarcia.proyectofinal.enlatadosmg.structures;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Cliente;
import java.util.ArrayList;
import java.util.List;

public class ArbolAVLClientes {
    private NodoArbolAVL raiz;

    public ArbolAVLClientes() {
        this.raiz = null;
    }

    public NodoArbolAVL obtenerRaiz() {
        return raiz;
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

    public int obtenerAltura(NodoArbolAVL nodo) {
        if (nodo == null) {
            return 0;
        }
        return nodo.obtenerAltura();
    }

    public int obtenerFactorBalance(NodoArbolAVL nodo) {
        if (nodo == null) {
            return 0;
        }
        return obtenerAltura(nodo.obtenerIzquierdo()) - obtenerAltura(nodo.obtenerDerecho());
    }

    public NodoArbolAVL rotacionDerecha(NodoArbolAVL y) {
        NodoArbolAVL x = y.obtenerIzquierdo();
        NodoArbolAVL T2 = x.obtenerDerecho();

        x.establecerDerecho(y);
        y.establecerIzquierdo(T2);

        y.establecerAltura(max(obtenerAltura(y.obtenerIzquierdo()), obtenerAltura(y.obtenerDerecho())) + 1);
        x.establecerAltura(max(obtenerAltura(x.obtenerIzquierdo()), obtenerAltura(x.obtenerDerecho())) + 1);

        return x;
    }

    public NodoArbolAVL rotacionIzquierda(NodoArbolAVL x) {
        NodoArbolAVL y = x.obtenerDerecho();
        NodoArbolAVL T2 = y.obtenerIzquierdo();

        y.establecerIzquierdo(x);
        x.establecerDerecho(T2);

        x.establecerAltura(max(obtenerAltura(x.obtenerIzquierdo()), obtenerAltura(x.obtenerDerecho())) + 1);
        y.establecerAltura(max(obtenerAltura(y.obtenerIzquierdo()), obtenerAltura(y.obtenerDerecho())) + 1);

        return y;
    }

    public NodoArbolAVL rotacionIzquierdaDerecha(NodoArbolAVL nodo) {
        nodo.establecerIzquierdo(rotacionIzquierda(nodo.obtenerIzquierdo()));
        return rotacionDerecha(nodo);
    }

    public NodoArbolAVL rotacionDerechaIzquierda(NodoArbolAVL nodo) {
        nodo.establecerDerecho(rotacionDerecha(nodo.obtenerDerecho()));
        return rotacionIzquierda(nodo);
    }

    public void insertar(Cliente cliente) {
        raiz = insertarRecursivo(raiz, cliente);
    }

    private NodoArbolAVL insertarRecursivo(NodoArbolAVL nodo, Cliente cliente) {
        if (nodo == null) {
            return new NodoArbolAVL(cliente);
        }

        int comparacion = cliente.getDpi().compareTo(nodo.obtenerCliente().getDpi());

        if (comparacion < 0) {
            nodo.establecerIzquierdo(insertarRecursivo(nodo.obtenerIzquierdo(), cliente));
        } else if (comparacion > 0) {
            nodo.establecerDerecho(insertarRecursivo(nodo.obtenerDerecho(), cliente));
        } else {
            nodo.establecerCliente(cliente);
            return nodo;
        }

        nodo.establecerAltura(1 + max(obtenerAltura(nodo.obtenerIzquierdo()), obtenerAltura(nodo.obtenerDerecho())));

        int balance = obtenerFactorBalance(nodo);

        // Caso Izquierda Izquierda (LL)
        if (balance > 1 && cliente.getDpi().compareTo(nodo.obtenerIzquierdo().obtenerCliente().getDpi()) < 0) {
            return rotacionDerecha(nodo);
        }

        // Caso Derecha Derecha (RR)
        if (balance < -1 && cliente.getDpi().compareTo(nodo.obtenerDerecho().obtenerCliente().getDpi()) > 0) {
            return rotacionIzquierda(nodo);
        }

        // Caso Izquierda Derecha (LR)
        if (balance > 1 && cliente.getDpi().compareTo(nodo.obtenerIzquierdo().obtenerCliente().getDpi()) > 0) {
            return rotacionIzquierdaDerecha(nodo);
        }

        // Caso Derecha Izquierda (RL)
        if (balance < -1 && cliente.getDpi().compareTo(nodo.obtenerDerecho().obtenerCliente().getDpi()) < 0) {
            return rotacionDerechaIzquierda(nodo);
        }

        return nodo;
    }

    public Cliente buscar(String dpi) {
        NodoArbolAVL nodo = buscarRecursivo(raiz, dpi);
        return (nodo != null) ? nodo.obtenerCliente() : null;
    }

    private NodoArbolAVL buscarRecursivo(NodoArbolAVL nodo, String dpi) {
        if (nodo == null || nodo.obtenerCliente().getDpi().equals(dpi)) {
            return nodo;
        }

        int comparacion = dpi.compareTo(nodo.obtenerCliente().getDpi());

        if (comparacion < 0) {
            return buscarRecursivo(nodo.obtenerIzquierdo(), dpi);
        } else {
            return buscarRecursivo(nodo.obtenerDerecho(), dpi);
        }
    }

    public void modificar(String dpi, Cliente clienteActualizado) {
        NodoArbolAVL nodo = buscarRecursivo(raiz, dpi);
        if (nodo != null) {
            nodo.establecerCliente(clienteActualizado);
        }
    }

    public void eliminar(String dpi) {
        raiz = eliminarRecursivo(raiz, dpi);
    }

    private NodoArbolAVL obtenerNodoMenorValor(NodoArbolAVL nodo) {
        NodoArbolAVL actual = nodo;
        while (actual.obtenerIzquierdo() != null) {
            actual = actual.obtenerIzquierdo();
        }
        return actual;
    }

    private NodoArbolAVL eliminarRecursivo(NodoArbolAVL raiz, String dpi) {
        if (raiz == null) {
            return raiz;
        }

        int comparacion = dpi.compareTo(raiz.obtenerCliente().getDpi());

        if (comparacion < 0) {
            raiz.establecerIzquierdo(eliminarRecursivo(raiz.obtenerIzquierdo(), dpi));
        } else if (comparacion > 0) {
            raiz.establecerDerecho(eliminarRecursivo(raiz.obtenerDerecho(), dpi));
        } else {
            if ((raiz.obtenerIzquierdo() == null) || (raiz.obtenerDerecho() == null)) {
                NodoArbolAVL temp = null;
                if (temp == raiz.obtenerIzquierdo()) {
                    temp = raiz.obtenerDerecho();
                } else {
                    temp = raiz.obtenerIzquierdo();
                }

                if (temp == null) {
                    temp = raiz;
                    raiz = null;
                } else {
                    raiz = temp;
                }
            } else {
                NodoArbolAVL temp = obtenerNodoMenorValor(raiz.obtenerDerecho());
                raiz.establecerCliente(temp.obtenerCliente());
                raiz.establecerDerecho(eliminarRecursivo(raiz.obtenerDerecho(), temp.obtenerCliente().getDpi()));
            }
        }

        if (raiz == null) {
            return raiz;
        }

        raiz.establecerAltura(max(obtenerAltura(raiz.obtenerIzquierdo()), obtenerAltura(raiz.obtenerDerecho())) + 1);

        int balance = obtenerFactorBalance(raiz);

        // Caso Izquierda Izquierda (LL)
        if (balance > 1 && obtenerFactorBalance(raiz.obtenerIzquierdo()) >= 0) {
            return rotacionDerecha(raiz);
        }

        // Caso Izquierda Derecha (LR)
        if (balance > 1 && obtenerFactorBalance(raiz.obtenerIzquierdo()) < 0) {
            return rotacionIzquierdaDerecha(raiz);
        }

        // Caso Derecha Derecha (RR)
        if (balance < -1 && obtenerFactorBalance(raiz.obtenerDerecho()) <= 0) {
            return rotacionIzquierda(raiz);
        }

        // Caso Derecha Izquierda (RL)
        if (balance < -1 && obtenerFactorBalance(raiz.obtenerDerecho()) > 0) {
            return rotacionDerechaIzquierda(raiz);
        }

        return raiz;
    }

    public List<Cliente> recorridoInorden() {
        List<Cliente> clientes = new ArrayList<>();
        recorridoInordenRecursivo(raiz, clientes);
        return clientes;
    }

    private void recorridoInordenRecursivo(NodoArbolAVL nodo, List<Cliente> clientes) {
        if (nodo != null) {
            recorridoInordenRecursivo(nodo.obtenerIzquierdo(), clientes);
            clientes.add(nodo.obtenerCliente());
            recorridoInordenRecursivo(nodo.obtenerDerecho(), clientes);
        }
    }
}
