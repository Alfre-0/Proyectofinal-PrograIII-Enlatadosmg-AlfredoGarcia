package com.alfredogarcia.proyectofinal.enlatadosmg.structures;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class ListaEnlazadaTest {

    private ListaEnlazada<String> lista;

    @Before
    public void setUp() {
        // Inicializa la lista antes de cada prueba
        lista = new ListaEnlazada<>();
    }

    @Test
    public void testListaVaciaAlInicio() {
        // Valida que la lista inicie vacía
        assertTrue("La lista debería estar vacía", lista.estaVacia());
        assertEquals("El tamaño debería ser 0", 0, lista.obtenerTamanio());
        assertNull("La cabeza de la lista debería ser null", lista.obtenerCabeza());
    }

    @Test
    public void testInsertarInicio() {
        // Valida la inserción al principio de la lista
        lista.insertarInicio("Uno");
        lista.insertarInicio("Dos");

        assertEquals("El tamaño debería ser 2", 2, lista.obtenerTamanio());
        assertEquals("La cabeza debería ser 'Dos' (el último insertado al inicio)", "Dos", lista.obtenerCabeza().obtenerDato());
    }

    @Test
    public void testInsertarFinal() {
        // Valida la inserción al final de la lista
        lista.insertarFinal("Uno");
        lista.insertarFinal("Dos");

        assertEquals("El tamaño debería ser 2", 2, lista.obtenerTamanio());
        assertEquals("La cabeza de la lista debería ser 'Uno'", "Uno", lista.obtenerCabeza().obtenerDato());
        assertEquals("El siguiente elemento de la cabeza debería ser 'Dos'", "Dos", lista.obtenerCabeza().obtenerSiguiente().obtenerDato());
    }

    @Test
    public void testEliminarNodo() {
        // Valida la eliminación de un nodo por condición
        lista.insertarFinal("Elemento 1");
        lista.insertarFinal("Elemento 2");
        lista.insertarFinal("Elemento 3");

        // Eliminar elemento del medio
        boolean eliminado = lista.eliminarNodo(s -> s.equals("Elemento 2"));
        assertTrue("Debería eliminar el elemento 2", eliminado);
        assertEquals("El tamaño debería reducirse a 2", 2, lista.obtenerTamanio());

        // Verificar que el elemento eliminado ya no está y el flujo se mantiene
        assertNull("No debería encontrar el Elemento 2", lista.buscar(s -> s.equals("Elemento 2")));
        assertEquals("El elemento siguiente al 1 debería ser el 3", "Elemento 3", lista.obtenerCabeza().obtenerSiguiente().obtenerDato());

        // Eliminar cabeza
        boolean cabezaEliminada = lista.eliminarNodo(s -> s.equals("Elemento 1"));
        assertTrue("Debería eliminar la cabeza", cabezaEliminada);
        assertEquals("La cabeza debería actualizarse a Elemento 3", "Elemento 3", lista.obtenerCabeza().obtenerDato());
    }

    @Test
    public void testBuscar() {
        // Valida que se busque correctamente un elemento
        lista.insertarFinal("Manzana");
        lista.insertarFinal("Pera");

        String encontrado = lista.buscar(s -> s.equals("Pera"));
        assertEquals("Debería encontrar 'Pera'", "Pera", encontrado);

        String noEncontrado = lista.buscar(s -> s.equals("Uva"));
        assertNull("Debería retornar null para 'Uva'", noEncontrado);
    }

    @Test
    public void testObtenerTodos() {
        // Valida que se retorne la lista de Java ordenada secuencialmente
        lista.insertarFinal("A");
        lista.insertarFinal("B");
        lista.insertarFinal("C");

        List<String> todos = lista.obtenerTodos();
        assertEquals("La lista debería contener 3 elementos", 3, todos.size());
        assertEquals("Primer elemento debería ser A", "A", todos.get(0));
        assertEquals("Segundo elemento debería ser B", "B", todos.get(1));
        assertEquals("Tercer elemento debería ser C", "C", todos.get(2));
    }
}
