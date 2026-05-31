package com.alfredogarcia.proyectofinal.enlatadosmg.service;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Vehiculo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VehiculoServiceTest {

    private VehiculoService vehiculoService;

    @Mock
    private CsvService csvService;

    @Before
    public void setUp() {
        // Inicializa las anotaciones de Mockito
        MockitoAnnotations.openMocks(this);
        // Construye el servicio inyectando el Mock de CsvService
        vehiculoService = new VehiculoService(csvService);
    }

    @Test
    public void testAgregarVehiculoExitoso() {
        // Valida la inserción exitosa de un vehículo
        Vehiculo v = new Vehiculo("P123ABC", "Toyota", "Yaris", "Gris", 2018, "Automatico");
        Vehiculo agregado = vehiculoService.agregar(v);

        assertNotNull("El vehículo agregado no debería ser nulo", agregado);
        assertEquals("La placa debe ser la misma", "P123ABC", agregado.getPlaca());

        // Validamos que se encuentre en las búsquedas
        Vehiculo buscado = vehiculoService.buscarPorPlaca("P123ABC");
        assertNotNull("El vehículo debería ser encontrado tras agregarlo", buscado);
    }

    @Test
    public void testAgregarVehiculoDuplicado() {
        // Valida que de error al agregar un vehículo con placa duplicada
        Vehiculo v1 = new Vehiculo("P123ABC", "Toyota", "Yaris", "Gris", 2018, "Automatico");
        Vehiculo v2 = new Vehiculo("P123ABC", "Honda", "Civic", "Azul", 2019, "Mecanico");

        vehiculoService.agregar(v1);
        try {
            vehiculoService.agregar(v2);
            fail("Debería lanzar error por placa duplicada");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("ya existe"));
        }
    }

    @Test
    public void testDesencolarYReencolar() {
        // Valida flujo de desencolado y reencolado
        Vehiculo v1 = new Vehiculo("P1", "Toyota", "Yaris", "Gris", 2018, "Automatico");
        Vehiculo v2 = new Vehiculo("P2", "Honda", "Civic", "Azul", 2019, "Mecanico");

        vehiculoService.agregar(v1);
        vehiculoService.agregar(v2);

        // Desencola el primero
        Vehiculo desencolado = vehiculoService.desencolar();
        assertEquals("Debería desencolar primero el Toyota (P1)", "P1", desencolado.getPlaca());

        // Reencolar Toyota
        vehiculoService.reencolar(desencolado);

        // Ahora el primero en la cola debería ser Honda (P2)
        Vehiculo desencolado2 = vehiculoService.desencolar();
        assertEquals("Debería desencolar ahora el Honda (P2)", "P2", desencolado2.getPlaca());
    }

    @Test
    public void testEliminarVehiculo() {
        // Valida la eliminación por placa
        Vehiculo v = new Vehiculo("P123ABC", "Toyota", "Yaris", "Gris", 2018, "Automatico");
        vehiculoService.agregar(v);

        vehiculoService.eliminar("P123ABC");
        assertNull("El vehículo ya no debería existir", vehiculoService.buscarPorPlaca("P123ABC"));
    }

    @Test
    public void testCargarDesdeCSV() {
        // Valida el parseo y cargado de vehículos desde CSV mockeado
        String csvContent = "Placa;Marca;Modelo;Color;Anio;Transmision\nP123ABC;Toyota;Yaris;Gris;2018;Automatico";
        List<String[]> lineasMock = new ArrayList<>();
        lineasMock.add(new String[] { "P123ABC", "Toyota", "Yaris", "Gris", "2018", "Automatico" });

        when(csvService.parsearLineas(csvContent)).thenReturn(lineasMock);

        vehiculoService.cargarDesdeCSV(csvContent);

        assertNotNull("El vehículo Toyota debería haberse cargado", vehiculoService.buscarPorPlaca("P123ABC"));
    }
}
