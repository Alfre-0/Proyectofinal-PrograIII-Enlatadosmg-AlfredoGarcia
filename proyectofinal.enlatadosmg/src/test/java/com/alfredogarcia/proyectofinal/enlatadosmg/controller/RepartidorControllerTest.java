package com.alfredogarcia.proyectofinal.enlatadosmg.controller;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Repartidor;
import com.alfredogarcia.proyectofinal.enlatadosmg.service.RepartidorService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RepartidorControllerTest {

    private RepartidorController controller;

    @Mock
    private RepartidorService service;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new RepartidorController(service);
    }

    @Test
    public void testObtenerTodos() {
        // Valida que el controlador retorne todos los repartidores
        when(service.obtenerTodos()).thenReturn(List.of());

        List<Repartidor> resultado = controller.obtenerTodos();

        assertNotNull("El listado no debería ser nulo", resultado);
        assertTrue("El listado debería estar vacío inicialmente", resultado.isEmpty());
    }

    @Test
    public void testObtenerPorDpiExitoso() {
        // Valida obtención de repartidor por DPI registrado
        Repartidor r = new Repartidor("123", "José", "García", "A", "5555");
        when(service.buscarPorDpi("123")).thenReturn(r);

        ResponseEntity<?> response = controller.obtenerPorDpi("123");

        assertEquals("El código HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertEquals("El cuerpo de la respuesta debería ser el repartidor", r, response.getBody());
    }

    @Test
    public void testCrearRepartidorExitoso() {
        // Valida creación de repartidor
        Repartidor r = new Repartidor("123", "José", "García", "A", "5555");
        when(service.agregar(r)).thenReturn(r);

        ResponseEntity<?> response = controller.crearRepartidor(r);

        assertEquals("El código HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertEquals("Debería retornar el repartidor creado", r, response.getBody());
    }
}
