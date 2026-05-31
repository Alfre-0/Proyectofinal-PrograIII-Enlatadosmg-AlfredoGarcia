package com.alfredogarcia.proyectofinal.enlatadosmg.structures;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Caja;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class ListaCajasPedidoTest {

    private ListaCajasPedido listaCajas;

    @Before
    public void setUp() {
        // Inicializa la lista antes de cada prueba
        listaCajas = new ListaCajasPedido();
    }

    @Test
    public void testListaVaciaAlInicio() {
        // Valida estado inicial vacío
        assertTrue("La lista de cajas de pedido debería estar vacía inicialmente", listaCajas.estaVacia());
        assertEquals("El tamaño inicial debería ser 0", 0, listaCajas.obtenerTamanio());
        assertNull("La cabeza debería ser null", listaCajas.obtenerCabeza());
    }

    @Test
    public void testAgregarCaja() {
        // Valida que se agreguen cajas al final de la estructura
        Caja caja1 = new Caja(101, "2026-05-24 10:00:00");
        Caja caja2 = new Caja(102, "2026-05-24 10:01:00");

        listaCajas.agregar(caja1);
        assertFalse("La lista no debería estar vacía", listaCajas.estaVacia());
        assertEquals("El tamaño debería ser 1", 1, listaCajas.obtenerTamanio());
        assertEquals("La cabeza debería ser la caja 101", 101, listaCajas.obtenerCabeza().obtenerDato().getCorrelativo());

        listaCajas.agregar(caja2);
        assertEquals("El tamaño debería ser 2", 2, listaCajas.obtenerTamanio());
        assertEquals("El siguiente de la cabeza debería ser la caja 102", 102, listaCajas.obtenerCabeza().obtenerSiguiente().obtenerDato().getCorrelativo());
    }

    @Test
    public void testObtenerTodas() {
        // Valida la obtención de la lista plana
        Caja caja1 = new Caja(1, "Fecha");
        Caja caja2 = new Caja(2, "Fecha");

        listaCajas.agregar(caja1);
        listaCajas.agregar(caja2);

        List<Caja> todas = listaCajas.obtenerTodas();
        assertEquals("La lista de retorno debería tener 2 elementos", 2, todas.size());
        assertEquals("El primer elemento debe ser correlativo 1", 1, todas.get(0).getCorrelativo());
        assertEquals("El segundo elemento debe ser correlativo 2", 2, todas.get(1).getCorrelativo());
    }
}
