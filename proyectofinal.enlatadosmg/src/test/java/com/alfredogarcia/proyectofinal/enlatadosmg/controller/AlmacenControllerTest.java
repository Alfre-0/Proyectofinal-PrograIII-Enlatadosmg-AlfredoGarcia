package com.alfredogarcia.proyectofinal.enlatadosmg.controller;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Caja;
import com.alfredogarcia.proyectofinal.enlatadosmg.service.AlmacenService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("null")
public class AlmacenControllerTest {

    private AlmacenController controller;

    @Mock
    private AlmacenService service;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new AlmacenController(service);
    }

    @Test
    public void testGenerarCajasExitoso() {
        // Valida que el endpoint retorne HTTP 200 al generar cajas exitosamente
        doNothing().when(service).generarCajas(10);

        ResponseEntity<?> response = controller.generarCajas(10);

        assertEquals("El estado HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertNotNull("El body no debería ser nulo", response.getBody());
        assertTrue("Debería retornar un mensaje de confirmación", ((Map<?, ?>) response.getBody()).get("mensaje").toString().contains("10 cajas"));
    }

    @Test
    public void testGenerarCajasFallido() {
        // Valida que el endpoint retorne HTTP 400 en caso de error del servicio
        doThrow(new RuntimeException("Error simulado")).when(service).generarCajas(-5);

        ResponseEntity<?> response = controller.generarCajas(-5);

        assertEquals("El estado HTTP debería ser 400 Bad Request", HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Debería incluir el mensaje de error", "Error simulado", ((Map<?, ?>) response.getBody()).get("error"));
    }

    @Test
    public void testObtenerCantidad() {
        // Valida que el endpoint retorne la cantidad correcta de cajas
        when(service.obtenerCantidad()).thenReturn(42);

        ResponseEntity<?> response = controller.obtenerCantidad();

        assertEquals("El estado HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertEquals("La cantidad debería ser 42", 42, ((Map<?, ?>) response.getBody()).get("cantidad"));
    }

    @Test
    public void testApilarCajaExitoso() {
        // Valida el apilado correcto de cajas
        Caja c = new Caja(1, "2026-05-24 10:00:00");
        when(service.apilarCaja()).thenReturn(c);

        ResponseEntity<?> response = controller.apilarCaja();

        assertEquals("El estado HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertEquals("Debería retornar la caja apilada", c, response.getBody());
    }
}
