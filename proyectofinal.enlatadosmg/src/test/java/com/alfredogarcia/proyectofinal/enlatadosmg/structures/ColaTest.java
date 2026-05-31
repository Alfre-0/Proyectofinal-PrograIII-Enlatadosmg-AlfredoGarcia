package com.alfredogarcia.proyectofinal.enlatadosmg.structures;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class ColaTest {

    private Cola<String> cola;

    @Before
    public void setUp() {
        // Inicializa la cola antes de cada prueba
        cola = new Cola<>();
    }

    @Test
    public void testColaVaciaAlInicio() {
        // Valida que la cola inicie vacía
        assertTrue("La cola debería estar vacía inicialmente", cola.estaVacia());
        assertEquals("El tamaño inicial debería ser 0", 0, cola.obtenerTamanio());
        assertNull("El frente debería ser null", cola.obtenerFrente());
    }

    @Test
    public void testEncolarYDesencolar() {
        // Valida la lógica FIFO (Primero en entrar, primero en salir)
        cola.encolar("Primero");
        cola.encolar("Segundo");

        assertEquals("El frente de la cola debería ser 'Primero'", "Primero", cola.obtenerFrente());
        assertEquals("El tamaño de la cola debería ser 2", 2, cola.obtenerTamanio());

        String desencolado1 = cola.desencolar();
        assertEquals("Debería desencolar primero el elemento que entró primero", "Primero", desencolado1);
        assertEquals("El frente de la cola debería actualizarse a 'Segundo'", "Segundo", cola.obtenerFrente());
        assertEquals("El tamaño debería ser 1", 1, cola.obtenerTamanio());

        String desencolado2 = cola.desencolar();
        assertEquals("Debería desencolar 'Segundo'", "Segundo", desencolado2);
        assertTrue("La cola debería quedar vacía", cola.estaVacia());
        assertNull("Desencolar en una cola vacía debería devolver null", cola.desencolar());
    }

    @Test
    public void testBuscar() {
        // Valida la búsqueda de elementos bajo un criterio/predicado
        cola.encolar("rojo");
        cola.encolar("azul");
        cola.encolar("verde");

        String encontrado = cola.buscar(s -> s.startsWith("a"));
        assertEquals("Debería encontrar 'azul'", "azul", encontrado);

        String inexistente = cola.buscar(s -> s.equals("amarillo"));
        assertNull("No debería encontrar 'amarillo'", inexistente);
    }

    @Test
    public void testEliminarNodo() {
        // Valida que se elimine un nodo en cualquier posición de la cola por criterio
        cola.encolar("A");
        cola.encolar("B");
        cola.encolar("C");

        // Eliminar el elemento del medio
        boolean eliminado = cola.eliminarNodo(s -> s.equals("B"));
        assertTrue("Debería haberse eliminado el elemento 'B'", eliminado);
        assertEquals("El tamaño debería ser 2", 2, cola.obtenerTamanio());
        assertEquals("El frente debe seguir siendo 'A'", "A", cola.obtenerFrente());

        // Eliminar el frente
        boolean eliminadoFrente = cola.eliminarNodo(s -> s.equals("A"));
        assertTrue("Debería haberse eliminado el elemento 'A'", eliminadoFrente);
        assertEquals("El frente nuevo debería ser 'C'", "C", cola.obtenerFrente());

        // Intentar eliminar inexistente
        assertFalse("No debería poder eliminar elemento que no existe", cola.eliminarNodo(s -> s.equals("Z")));
    }

    @Test
    public void testObtenerTodos() {
        // Valida la conversión a lista en el orden de la cola
        cola.encolar("X");
        cola.encolar("Y");

        List<String> todos = cola.obtenerTodos();
        assertEquals("Debería tener 2 elementos", 2, todos.size());
        assertEquals("El primero debería ser 'X'", "X", todos.get(0));
        assertEquals("El segundo debería ser 'Y'", "Y", todos.get(1));
    }
}
