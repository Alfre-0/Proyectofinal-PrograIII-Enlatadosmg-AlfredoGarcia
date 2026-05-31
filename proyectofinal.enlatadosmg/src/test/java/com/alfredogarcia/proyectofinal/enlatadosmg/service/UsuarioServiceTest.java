package com.alfredogarcia.proyectofinal.enlatadosmg.service;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Usuario;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    private UsuarioService usuarioService;

    @Mock
    private CsvService csvService;

    @Before
    public void setUp() {
        // Inicializa las anotaciones de Mockito
        MockitoAnnotations.openMocks(this);
        // Construye el servicio inyectando el Mock de CsvService
        usuarioService = new UsuarioService(csvService);
    }

    @Test
    public void testPrecargaUsuarioAdmin() {
        // Valida que el usuario administrador inicial (ID 1) ya esté cargado
        Usuario admin = usuarioService.buscarPorId(1);
        assertNotNull("El administrador inicial debería existir", admin);
        assertEquals("El nombre del admin inicial debería ser Admin", "Admin", admin.getNombre());
    }

    @Test
    public void testCrearUsuarioExitoso() {
        // Valida la inserción exitosa de un nuevo usuario
        Usuario u = new Usuario(2, "Pedro", "Gómez", "abcd");
        Usuario creado = usuarioService.crearUsuario(u);

        assertNotNull("El usuario creado no debería ser nulo", creado);
        assertEquals("El ID debe ser 2", 2, creado.getId());

        Usuario buscado = usuarioService.buscarPorId(2);
        assertNotNull("Debería encontrar al usuario Pedro", buscado);
    }

    @Test
    public void testCrearUsuarioDuplicado() {
        // Valida que dé error al crear usuario con ID duplicado
        Usuario u = new Usuario(1, "Clon", "Admin", "1111"); // ID 1 ya está precargado
        try {
            usuarioService.crearUsuario(u);
            fail("Debería lanzar error por ID duplicado");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("ya existe"));
        }
    }

    @Test
    public void testLoginExitosoYFallido() {
        // Valida logueo exitoso del admin precargado
        Usuario u = usuarioService.login(1, "1234");
        assertNotNull("El login de admin debería ser exitoso", u);
        assertEquals("Debería retornar el usuario Admin", "Admin", u.getNombre());

        // Valida logueo con contraseña incorrecta
        Usuario uFallido = usuarioService.login(1, "contrasena_incorrecta");
        assertNull("El login debería fallar y retornar null", uFallido);
    }

    @Test
    public void testCargarDesdeCSV() {
        // Valida el parseo y cargado de usuarios desde CSV mockeado
        String csvContent = "Id;Nombre;Apellido;Contrasena\n3;Juan;Perez;pass123";
        List<String[]> lineasMock = new ArrayList<>();
        lineasMock.add(new String[]{"3", "Juan", "Perez", "pass123"});

        when(csvService.parsearLineas(csvContent)).thenReturn(lineasMock);

        usuarioService.cargarDesdeCSV(csvContent);

        assertNotNull("El usuario Juan debería haberse cargado", usuarioService.buscarPorId(3));
    }
}
