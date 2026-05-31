package com.alfredogarcia.proyectofinal.enlatadosmg.controller;

import com.alfredogarcia.proyectofinal.enlatadosmg.dto.CrearPedidoRequest;
import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Pedido;
import com.alfredogarcia.proyectofinal.enlatadosmg.service.PedidoService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PedidoControllerTest {

    private PedidoController controller;

    @Mock
    private PedidoService service;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new PedidoController(service);
    }

    @Test
    public void testObtenerTodos() {
        // Valida que el endpoint retorne todos los pedidos del servicio
        when(service.obtenerTodos()).thenReturn(List.of());

        List<Pedido> resultado = controller.obtenerTodos();

        assertNotNull("La lista no debería ser nula", resultado);
        assertTrue("La lista debería estar vacía inicialmente", resultado.isEmpty());
    }

    @Test
    public void testCrearPedidoExitoso() {
        // Valida la creación de un pedido mapeando el request
        CrearPedidoRequest req = new CrearPedidoRequest("123", "Gua", "Esc", 5);
        Pedido p = new Pedido(1, "Gua", "Esc", "Fecha", null, null, null, null, 5, null);
        when(service.crearPedido(req)).thenReturn(p);

        ResponseEntity<?> response = controller.crearPedido(req);

        assertEquals("El código HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertEquals("Debería retornar el pedido creado", p, response.getBody());
    }

    @Test
    public void testCompletarPedidoExitoso() {
        // Valida la llamada de completar pedido
        Pedido p = new Pedido(1, "Gua", "Esc", "Fecha", null, null, null, null, 5, null);
        when(service.completarPedido(1)).thenReturn(p);

        ResponseEntity<?> response = controller.completarPedido(1);

        assertEquals("El código HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertEquals("Debería retornar el pedido completado", p, response.getBody());
    }
}
