package com.alfredogarcia.proyectofinal.enlatadosmg.structures;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Cliente;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class ArbolAVLClientesTest {

    private ArbolAVLClientes arbol;

    @Before
    public void setUp() {
        // Inicializa el árbol vacío antes de cada prueba
        arbol = new ArbolAVLClientes();
    }

    @Test
    public void testArbolVacioAlInicio() {
        // Valida que un árbol nuevo esté vacío y su raíz sea nula
        assertTrue("El árbol debería estar vacío inicialmente", arbol.estaVacio());
        assertNull("La raíz debería ser nula", arbol.obtenerRaiz());
    }

    @Test
    public void testInsertarYBuscarCliente() {
        // Valida que se puedan insertar y recuperar clientes por DPI
        Cliente cliente1 = new Cliente("1234567890101", "Ana", "Rodríguez", "55551111", "Guatemala");
        Cliente cliente2 = new Cliente("2345678901012", "Roberto", "Flores", "55552222", "Antigua");

        arbol.insertar(cliente1);
        arbol.insertar(cliente2);

        assertFalse("El árbol no debería estar vacío tras insertar elementos", arbol.estaVacio());

        Cliente buscado1 = arbol.buscar("1234567890101");
        assertNotNull("Debería encontrar al cliente Ana", buscado1);
        assertEquals("El nombre debería ser Ana", "Ana", buscado1.getNombre());

        Cliente buscado2 = arbol.buscar("2345678901012");
        assertNotNull("Debería encontrar al cliente Roberto", buscado2);

        Cliente buscadoInexistente = arbol.buscar("9999999999999");
        assertNull("No debería encontrar un cliente no registrado", buscadoInexistente);
    }

    @Test
    public void testRecorridoInordenOrdenadoPorDPI() {
        // Valida que el recorrido inorden devuelva la lista ordenada ascendentemente por DPI (característica de árbol binario de búsqueda)
        Cliente c1 = new Cliente("2222222222222", "B", "B", "123", "Dir");
        Cliente c2 = new Cliente("1111111111111", "A", "A", "123", "Dir");
        Cliente c3 = new Cliente("3333333333333", "C", "C", "123", "Dir");

        arbol.insertar(c1);
        arbol.insertar(c2);
        arbol.insertar(c3);

        List<Cliente> listado = arbol.recorridoInorden();
        assertEquals("Deberían haber 3 clientes", 3, listado.size());
        assertEquals("El primero debería ser el DPI menor (111...)", "1111111111111", listado.get(0).getDpi());
        assertEquals("El segundo debería ser el intermedio (222...)", "2222222222222", listado.get(1).getDpi());
        assertEquals("El tercero debería ser el mayor (333...)", "3333333333333", listado.get(2).getDpi());
    }

    @Test
    public void testEliminarCliente() {
        // Valida que se elimine un nodo del árbol correctamente y se reorganice
        Cliente c1 = new Cliente("22", "B", "B", "123", "Dir");
        Cliente c2 = new Cliente("11", "A", "A", "123", "Dir");
        Cliente c3 = new Cliente("33", "C", "C", "123", "Dir");

        arbol.insertar(c1);
        arbol.insertar(c2);
        arbol.insertar(c3);

        arbol.eliminar("22");
        assertNull("El cliente '22' ya no debería existir en el árbol", arbol.buscar("22"));
        assertNotNull("El cliente '11' debería seguir existiendo", arbol.buscar("11"));
        assertNotNull("El cliente '33' debería seguir existiendo", arbol.buscar("33"));
    }

    @Test
    public void testModificarCliente() {
        // Valida la modificación de datos de un cliente existente
        Cliente c = new Cliente("123", "Nombre Original", "Apellido", "1", "Dir");
        arbol.insertar(c);

        Cliente cModificado = new Cliente("123", "Nombre Nuevo", "Apellido Nuevo", "2", "Dir Nueva");
        arbol.modificar("123", cModificado);

        Cliente buscado = arbol.buscar("123");
        assertEquals("El nombre debería haberse actualizado", "Nombre Nuevo", buscado.getNombre());
        assertEquals("El teléfono debería haberse actualizado", "2", buscado.getTelefono());
    }
}
