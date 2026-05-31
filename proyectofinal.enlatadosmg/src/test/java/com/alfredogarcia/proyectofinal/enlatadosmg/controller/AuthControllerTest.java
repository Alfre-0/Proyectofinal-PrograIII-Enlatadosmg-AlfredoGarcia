package com.alfredogarcia.proyectofinal.enlatadosmg.controller;

import com.alfredogarcia.proyectofinal.enlatadosmg.dto.LoginRequest;
import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Usuario;
import com.alfredogarcia.proyectofinal.enlatadosmg.service.UsuarioService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("null")
public class AuthControllerTest {

    private AuthController controller;

    @Mock
    private UsuarioService service;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new AuthController(service);
    }

    @Test
    public void testLoginExitoso() {
        // Valida que el login retorne HTTP 200 con el objeto de usuario al autenticarse correctamente
        Usuario u = new Usuario(1, "Pedro", "Gomez", "contrasena");
        when(service.login(1, "contrasena")).thenReturn(u);

        LoginRequest req = new LoginRequest(1, "contrasena");
        ResponseEntity<?> response = controller.login(req);

        assertEquals("El estado HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertEquals("Debería retornar el objeto de usuario", u, response.getBody());
    }

    @Test
    public void testLoginFallido() {
        // Valida que el login retorne HTTP 401 si las credenciales son incorrectas
        when(service.login(1, "malo")).thenReturn(null);

        LoginRequest req = new LoginRequest(1, "malo");
        ResponseEntity<?> response = controller.login(req);

        assertEquals("El estado HTTP debería ser 401 Unauthorized", HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue("Debería retornar error en JSON", response.getBody().toString().contains("incorrectos"));
    }

    @Test
    public void testObtenerPerfilExitoso() {
        // Valida que se retorne el perfil de usuario si el ID existe
        Usuario u = new Usuario(10, "Pedro", "Gomez", "pass");
        when(service.buscarPorId(10)).thenReturn(u);

        ResponseEntity<?> response = controller.obtenerPerfil(10);

        assertEquals("El estado HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertEquals("El cuerpo de la respuesta debería ser el usuario", u, response.getBody());
    }

    @Test
    public void testObtenerPerfilInexistente() {
        // Valida que se retorne HTTP 404 si el usuario no existe
        when(service.buscarPorId(99)).thenReturn(null);

        ResponseEntity<?> response = controller.obtenerPerfil(99);

        assertEquals("El estado HTTP debería ser 404 Not Found", HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue("Debería indicar que el usuario no fue encontrado", response.getBody().toString().contains("no encontrado"));
    }
}
