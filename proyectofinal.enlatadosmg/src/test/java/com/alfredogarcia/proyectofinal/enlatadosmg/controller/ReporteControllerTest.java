package com.alfredogarcia.proyectofinal.enlatadosmg.controller;

import com.alfredogarcia.proyectofinal.enlatadosmg.service.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReporteControllerTest {

    private ReporteController controller;

    @Mock private DotService dotService;
    @Mock private UsuarioService usuarioService;
    @Mock private AlmacenService almacenService;
    @Mock private ClienteService clienteService;
    @Mock private RepartidorService repartidorService;
    @Mock private VehiculoService vehiculoService;
    @Mock private PedidoService pedidoService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ReporteController(dotService, usuarioService, almacenService,
                clienteService, repartidorService, vehiculoService, pedidoService);
    }

    @Test
    public void testReporteUsuarios() {
        // Valida que el controlador retorne el DOT para el reporte de usuarios
        when(usuarioService.obtenerTodos()).thenReturn(List.of());
        when(dotService.generarDotUsuarios(anyList())).thenReturn("digraph G {}");

        ResponseEntity<String> response = controller.reporteUsuarios();

        assertEquals("El código HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertEquals("El cuerpo de la respuesta debería ser el código DOT", "digraph G {}", response.getBody());
    }

    @Test
    public void testReporteAlmacen() {
        // Valida que el controlador retorne el DOT para el reporte de almacén
        when(almacenService.obtenerTodas()).thenReturn(List.of());
        when(dotService.generarDotAlmacen(anyList())).thenReturn("digraph G { almacen }");

        ResponseEntity<String> response = controller.reporteAlmacen();

        assertEquals("El código HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertEquals("El cuerpo de la respuesta debería ser el código DOT", "digraph G { almacen }", response.getBody());
    }
}
