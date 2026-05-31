package com.alfredogarcia.proyectofinal.enlatadosmg.controller;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Vehiculo;
import com.alfredogarcia.proyectofinal.enlatadosmg.service.VehiculoService;
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
public class VehiculoControllerTest {

    private VehiculoController controller;

    @Mock
    private VehiculoService service;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new VehiculoController(service);
    }

    @Test
    public void testObtenerTodos() {
        // Valida que el controlador retorne todos los vehículos del servicio
        Vehiculo v = new Vehiculo("P123ABC", "Toyota", "Hilux", "Blanco", 2020, "Manual");
        when(service.obtenerTodos()).thenReturn(List.of(v));

        List<Vehiculo> resultado = controller.obtenerTodos();

        assertEquals("Debería retornar 1 vehículo", 1, resultado.size());
        assertEquals("La placa debería coincidir", "P123ABC", resultado.get(0).getPlaca());
    }

    @Test
    public void testObtenerPorPlacaExitoso() {
        // Valida obtención de vehículo por placa registrada
        Vehiculo v = new Vehiculo("P123ABC", "Toyota", "Hilux", "Blanco", 2020, "Manual");
        when(service.buscarPorPlaca("P123ABC")).thenReturn(v);

        ResponseEntity<?> response = controller.obtenerPorPlaca("P123ABC");

        assertEquals("El código HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertEquals("El cuerpo debería ser el vehículo", v, response.getBody());
    }

    @Test
    public void testObtenerPorPlacaInexistente() {
        // Valida que retorne 404 para placa no registrada
        when(service.buscarPorPlaca("ZZZ")).thenReturn(null);

        ResponseEntity<?> response = controller.obtenerPorPlaca("ZZZ");

        assertEquals("El código HTTP debería ser 404 Not Found", HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCrearVehiculoExitoso() {
        // Valida creación de vehículo correctamente
        Vehiculo v = new Vehiculo("P123ABC", "Toyota", "Hilux", "Blanco", 2020, "Manual");
        when(service.agregar(v)).thenReturn(v);

        ResponseEntity<?> response = controller.crearVehiculo(v);

        assertEquals("El código HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertEquals("El cuerpo debería ser el vehículo creado", v, response.getBody());
    }

    @Test
    public void testEliminarVehiculoExitoso() {
        // Valida eliminación correcta de un vehículo
        doNothing().when(service).eliminar("P123ABC");

        ResponseEntity<?> response = controller.eliminarVehiculo("P123ABC");

        assertEquals("El código HTTP debería ser 200 OK", HttpStatus.OK, response.getStatusCode());
        assertTrue("Debería indicar eliminación exitosa", response.getBody().toString().contains("eliminado"));
    }

    @Test
    public void testEliminarVehiculoInexistenteDaError() {
        // Valida que retorne 400 si el vehículo no existe al eliminar
        doThrow(new RuntimeException("El vehículo con placa ZZZ no existe.")).when(service).eliminar("ZZZ");

        ResponseEntity<?> response = controller.eliminarVehiculo("ZZZ");

        assertEquals("El código HTTP debería ser 400 Bad Request", HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
