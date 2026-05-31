package com.alfredogarcia.proyectofinal.enlatadosmg.service;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Caja;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class AlmacenServiceTest {

    private AlmacenService almacenService;

    @Before
    public void setUp() {
        // Inicializa el servicio antes de cada prueba
        almacenService = new AlmacenService();
    }

    @Test
    public void testGenerarCajas() {
        // Valida la generación correcta de cajas
        almacenService.generarCajas(5);
        assertEquals("El almacén debería contener exactamente 5 cajas", 5, almacenService.obtenerCantidad());

        List<Caja> cajas = almacenService.obtenerTodas();
        assertEquals("Deberían retornar 5 cajas", 5, cajas.size());
        // Como es una pila (LIFO), la última caja generada (correlativo 5) debería estar en la cima (primera en la lista)
        assertEquals("La primera caja de la lista de retorno debería ser la última generada (id 5)", 5, cajas.get(0).getCorrelativo());
    }

    @Test
    public void testGenerarCajasInvalido() {
        // Valida error al intentar generar cantidades negativas o cero
        try {
            almacenService.generarCajas(0);
            fail("Debería lanzar error por cantidad <= 0");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("mayor que cero"));
        }
    }

    @Test
    public void testApilarYDesapilarCaja() {
        // Valida apilado y desapilado individual de cajas
        Caja apilada = almacenService.apilarCaja();
        assertEquals("La caja apilada debe tener ID correlativo 1", 1, apilada.getCorrelativo());
        assertEquals("La cantidad en el almacén debe ser 1", 1, almacenService.obtenerCantidad());

        Caja desapilada = almacenService.desapilarCaja();
        assertEquals("La caja desapilada debe ser la misma que se apiló", apilada.getCorrelativo(), desapilada.getCorrelativo());
        assertEquals("La cantidad debe ser 0", 0, almacenService.obtenerCantidad());
    }

    @Test
    public void testDesapilarCajaVacia() {
        // Valida que dé error al desapilar cuando el almacén esté vacío
        try {
            almacenService.desapilarCaja();
            fail("Debería lanzar error al desapilar de un almacén vacío");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("No hay cajas"));
        }
    }
}
