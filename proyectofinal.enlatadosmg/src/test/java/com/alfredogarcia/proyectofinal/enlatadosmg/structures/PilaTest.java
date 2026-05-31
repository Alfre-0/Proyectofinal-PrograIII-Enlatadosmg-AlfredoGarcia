package com.alfredogarcia.proyectofinal.enlatadosmg.structures;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class PilaTest {

    private Pila<Integer> pila;

    @Before
    public void setUp() {
        // Inicializa la pila antes de cada prueba
        pila = new Pila<>();
    }

    @Test
    public void testPilaVaciaAlInicio() {
        // Valida que una nueva pila esté vacía y su tamaño sea 0
        assertTrue("La pila debería estar vacía inicialmente", pila.estaVacia());
        assertEquals("El tamaño de una pila vacía debería ser 0", 0, pila.obtenerTamanio());
        assertNull("La cima de una pila vacía debería ser null", pila.obtenerCima());
    }

    @Test
    public void testApilarElementos() {
        // Valida que los elementos se apilen correctamente y aumente el tamaño
        pila.apilar(10);
        assertFalse("La pila no debería estar vacía después de apilar", pila.estaVacia());
        assertEquals("El tamaño debería ser 1 después de apilar un elemento", 1, pila.obtenerTamanio());
        assertEquals("La cima debería ser el último elemento apilado", Integer.valueOf(10), pila.obtenerCima());

        pila.apilar(20);
        assertEquals("El tamaño debería ser 2", 2, pila.obtenerTamanio());
        assertEquals("La cima debería actualizarse al nuevo elemento", Integer.valueOf(20), pila.obtenerCima());
    }

    @Test
    public void testDesapilarElementos() {
        // Valida el funcionamiento LIFO (último en entrar, primero en salir)
        pila.apilar(5);
        pila.apilar(15);

        Integer desapilado1 = pila.desapilar();
        assertEquals("Debería desapilar primero el último elemento ingresado (15)", Integer.valueOf(15), desapilado1);
        assertEquals("El tamaño debería reducirse a 1", 1, pila.obtenerTamanio());

        Integer desapilado2 = pila.desapilar();
        assertEquals("Debería desapilar el elemento restante (5)", Integer.valueOf(5), desapilado2);
        assertTrue("La pila debería quedar vacía", pila.estaVacia());

        assertNull("Desapilar en una pila vacía debería retornar null", pila.desapilar());
    }

    @Test
    public void testObtenerTodos() {
        // Valida que se obtengan todos los elementos en el orden correcto de la pila (de cima a fondo)
        pila.apilar(1);
        pila.apilar(2);
        pila.apilar(3);

        List<Integer> todos = pila.obtenerTodos();
        assertEquals("La lista debería tener 3 elementos", 3, todos.size());
        assertEquals("El primer elemento de la lista debe ser la cima (3)", Integer.valueOf(3), todos.get(0));
        assertEquals("El segundo elemento debe ser (2)", Integer.valueOf(2), todos.get(1));
        assertEquals("El tercer elemento debe ser (1)", Integer.valueOf(1), todos.get(2));
    }
}
