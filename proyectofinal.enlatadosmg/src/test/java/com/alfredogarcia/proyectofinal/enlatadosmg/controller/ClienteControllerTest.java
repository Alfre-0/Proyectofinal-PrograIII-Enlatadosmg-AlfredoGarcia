package com.alfredogarcia.proyectofinal.enlatadosmg.controller;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Cliente;
import com.alfredogarcia.proyectofinal.enlatadosmg.service.ClienteService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("null")
public class ClienteControllerTest {

    private ClienteController controller;

    @Mock
    private ClienteService service;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ClienteController(service);
    }

    @Test
    public void testObtenerTodos() {
        // Valida que el controlador retorne el listado de clientes provisto por el servicio
        Cliente c = new Cliente("123", "Ana", "R", "1", "Gua");
        when(service.listarTodos()).thenReturn(List.of(c));

        List<Cliente> resultado = controller.obtenerTodos();

        assertEquals("Debería retornar 1 cliente", 1, resultado.size());
        assertEquals("El cliente debería ser Ana", "Ana", resultado.get(0).getNombre());
    }

    @Test
    public void testObtenerPorDpiExitoso() {
        // Valida la obtención de cliente por DPI existente
        Cliente c = new Cliente("123", "Ana", "R", "1", "Gua");
        when(service.buscar("123")).thenReturn(c);

        ResponseEntity<?> response = controller.obtenerPorDpi("123");

        assertEquals("El código HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertEquals("El cuerpo de la respuesta debería ser el cliente", c, response.getBody());
    }

    @Test
    public void testObtenerPorDpiInexistente() {
        // Valida que retorne 404 para DPI no registrado
        when(service.buscar("99")).thenReturn(null);

        ResponseEntity<?> response = controller.obtenerPorDpi("99");

        assertEquals("El código HTTP debería ser 404 Not Found", HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCrearClienteExitoso() {
        // Valida creación de cliente
        Cliente c = new Cliente("123", "Ana", "R", "1", "Gua");
        when(service.insertar(c)).thenReturn(c);

        ResponseEntity<?> response = controller.crearCliente(c);

        assertEquals("El código HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertEquals("Debería retornar el cliente creado", c, response.getBody());
    }

    @Test
    public void testEliminarClienteExitoso() {
        // Valida eliminación de cliente
        doNothing().when(service).eliminar("123");

        ResponseEntity<?> response = controller.eliminarCliente("123");

        assertEquals("El código HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("eliminado correctamente"));
    }
}
