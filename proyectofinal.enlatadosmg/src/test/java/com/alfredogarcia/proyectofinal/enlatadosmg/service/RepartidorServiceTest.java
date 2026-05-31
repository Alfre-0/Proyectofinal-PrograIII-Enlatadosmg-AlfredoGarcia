package com.alfredogarcia.proyectofinal.enlatadosmg.service;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Repartidor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RepartidorServiceTest {

    private RepartidorService repartidorService;

    @Mock
    private CsvService csvService;

    @Before
    public void setUp() {
        // Inicializa las anotaciones de Mockito
        MockitoAnnotations.openMocks(this);
        // Construye el servicio inyectando el Mock de CsvService
        repartidorService = new RepartidorService(csvService);
    }

    @Test
    public void testAgregarRepartidorExitoso() {
        // Valida la inserción exitosa de un repartidor a la cola
        Repartidor r = new Repartidor("1111111111111", "José", "García", "A", "55554444");
        Repartidor agregado = repartidorService.agregar(r);

        assertNotNull("El repartidor agregado no debería ser nulo", agregado);
        assertEquals("El DPI debe ser el mismo", "1111111111111", agregado.getDpi());

        // Validamos que se encuentre en las búsquedas
        Repartidor buscado = repartidorService.buscarPorDpi("1111111111111");
        assertNotNull("El repartidor debería ser encontrado tras agregarlo", buscado);
    }

    @Test
    public void testAgregarRepartidorDuplicado() {
        // Valida que de error al agregar un repartidor duplicado
        Repartidor r1 = new Repartidor("123", "José", "García", "A", "55554444");
        Repartidor r2 = new Repartidor("123", "Mario", "García", "B", "55554444");

        repartidorService.agregar(r1);
        try {
            repartidorService.agregar(r2);
            fail("Debería lanzar error por DPI duplicado");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("ya existe"));
        }
    }

    @Test
    public void testDesencolarYReencolar() {
        // Valida flujo de desencolado y reencolado
        Repartidor r1 = new Repartidor("123", "José", "García", "A", "1");
        Repartidor r2 = new Repartidor("456", "Mario", "García", "B", "2");

        repartidorService.agregar(r1);
        repartidorService.agregar(r2);

        // Desencola el primero
        Repartidor desencolado = repartidorService.desencolar();
        assertEquals("Debería desencolar primero a José (ID 123)", "123", desencolado.getDpi());

        // Reencolar a José
        repartidorService.reencolar(desencolado);

        // Ahora el primero en la cola debería ser Mario (ID 456)
        Repartidor desencolado2 = repartidorService.desencolar();
        assertEquals("Debería desencolar ahora a Mario (ID 456)", "456", desencolado2.getDpi());
    }

    @Test
    public void testEliminarRepartidor() {
        // Valida la eliminación por DPI
        Repartidor r = new Repartidor("123", "José", "García", "A", "1");
        repartidorService.agregar(r);

        repartidorService.eliminar("123");
        assertNull("El repartidor ya no debería existir", repartidorService.buscarPorDpi("123"));
    }

    @Test
    public void testCargarDesdeCSV() {
        // Valida el parseo y cargado de repartidores desde CSV mockeado
        String csvContent = "Dpi;Nombre;Apellido;Licencia;Telefono\n123;Jose;Garcia;A;5555";
        List<String[]> lineasMock = new ArrayList<>();
        lineasMock.add(new String[] { "123", "Jose", "Garcia", "A", "5555" });

        when(csvService.parsearLineas(csvContent)).thenReturn(lineasMock);

        repartidorService.cargarDesdeCSV(csvContent);

        assertNotNull("El repartidor Jose debería haberse cargado", repartidorService.buscarPorDpi("123"));
    }
}
