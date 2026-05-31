package com.alfredogarcia.proyectofinal.enlatadosmg.controller;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Usuario;
import com.alfredogarcia.proyectofinal.enlatadosmg.service.UsuarioService;
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
public class UsuarioControllerTest {

    private UsuarioController controller;

    @Mock
    private UsuarioService service;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new UsuarioController(service);
    }

    @Test
    public void testObtenerTodos() {
        // Valida que el controlador retorne todos los usuarios del servicio
        Usuario u = new Usuario(1, "Admin", "General", "1234");
        when(service.obtenerTodos()).thenReturn(List.of(u));

        List<Usuario> resultado = controller.obtenerTodos();

        assertEquals("Debería retornar 1 usuario", 1, resultado.size());
        assertEquals("El nombre debería coincidir", "Admin", resultado.get(0).getNombre());
    }

    @Test
    public void testObtenerPorIdExitoso() {
        // Valida obtención de usuario por ID existente
        Usuario u = new Usuario(1, "Admin", "General", "1234");
        when(service.buscarPorId(1)).thenReturn(u);

        ResponseEntity<?> response = controller.obtenerPorId(1);

        assertEquals("El código HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertEquals("El cuerpo debería ser el usuario", u, response.getBody());
    }

    @Test
    public void testObtenerPorIdInexistente() {
        // Valida que retorne 404 si el usuario no existe
        when(service.buscarPorId(999)).thenReturn(null);

        ResponseEntity<?> response = controller.obtenerPorId(999);

        assertEquals("El código HTTP debería ser 404 Not Found", HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCrearUsuarioExitoso() {
        // Valida creación de usuario exitosa
        Usuario u = new Usuario(2, "Pedro", "Gómez", "abcd");
        when(service.crearUsuario(u)).thenReturn(u);

        ResponseEntity<?> response = controller.crearUsuario(u);

        assertEquals("El código HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertEquals("El cuerpo debería ser el usuario creado", u, response.getBody());
    }

    @Test
    public void testCrearUsuarioDuplicadoRetornaBadRequest() {
        // Valida que retorne 400 si el ID ya está en uso
        Usuario u = new Usuario(1, "Clon", "Admin", "xxx");
        when(service.crearUsuario(u)).thenThrow(new RuntimeException("El ID de usuario ya existe: 1"));

        ResponseEntity<?> response = controller.crearUsuario(u);

        assertEquals("El código HTTP debería ser 400 Bad Request", HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue("Debería incluir el mensaje de error", response.getBody().toString().contains("ya existe"));
    }
}
