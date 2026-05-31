package com.alfredogarcia.proyectofinal.enlatadosmg.service;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Cliente;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ClienteServiceTest {

    private ClienteService clienteService;

    @Mock
    private CsvService csvService;

    @Before
    public void setUp() {
        // Inicializa las anotaciones de Mockito
        MockitoAnnotations.openMocks(this);
        // Construye el servicio inyectando el Mock de CsvService
        clienteService = new ClienteService(csvService);
    }

    @Test
    public void testInsertarClienteExitoso() {
        // Valida la inserción exitosa de un cliente
        Cliente c = new Cliente("1111111111111", "Juan", "Pérez", "55554444", "Gua");
        Cliente insertado = clienteService.insertar(c);

        assertNotNull("El cliente insertado no debería ser nulo", insertado);
        assertEquals("El DPI debe ser el mismo", "1111111111111", insertado.getDpi());

        // Validamos que se encuentre en las búsquedas
        Cliente buscado = clienteService.buscar("1111111111111");
        assertNotNull("El cliente debería ser encontrado tras la inserción", buscado);
    }

    @Test
    public void testInsertarClienteDpiNulo() {
        // Valida que de error al insertar cliente con DPI nulo o vacío
        Cliente c = new Cliente("", "Juan", "Pérez", "55554444", "Gua");
        try {
            clienteService.insertar(c);
            fail("Debería lanzar error por DPI vacío");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("DPI no puede ser nulo o vacío"));
        }
    }

    @Test
    public void testInsertarClienteDuplicado() {
        // Valida que de error al insertar cliente con DPI duplicado
        Cliente c1 = new Cliente("123", "Juan", "Pérez", "55554444", "Gua");
        Cliente c2 = new Cliente("123", "Carlos", "Pérez", "55554444", "Gua");

        clienteService.insertar(c1);
        try {
            clienteService.insertar(c2);
            fail("Debería lanzar error por DPI duplicado");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("ya existe"));
        }
    }

    @Test
    public void testModificarCliente() {
        // Valida modificación sin cambio de DPI
        Cliente c = new Cliente("123", "Original", "Original", "123", "Gua");
        clienteService.insertar(c);

        Cliente modificado = new Cliente("123", "Modificado", "Original", "123", "Gua");
        clienteService.modificar("123", modificado);

        Cliente buscado = clienteService.buscar("123");
        assertEquals("El nombre debería haberse actualizado", "Modificado", buscado.getNombre());
    }

    @Test
    public void testEliminarCliente() {
        // Valida la eliminación lógica del cliente
        Cliente c = new Cliente("123", "Juan", "Pérez", "123", "Gua");
        clienteService.insertar(c);

        clienteService.eliminar("123");
        assertNull("El cliente no debería encontrarse tras ser eliminado", clienteService.buscar("123"));
    }

    @Test
    public void testCargarDesdeCSV() {
        // Valida el parseo y cargado de clientes desde CSV mockeado
        String csvContent = "123,Juan,Perez,5555\n456,Maria,Gomez,7777";
        List<String[]> lineasMock = new ArrayList<>();
        lineasMock.add(new String[] { "123", "Juan", "Perez", "5555" });
        lineasMock.add(new String[] { "456", "Maria", "Gomez", "7777", "Antigua" });

        when(csvService.parsearLineas(csvContent)).thenReturn(lineasMock);

        clienteService.cargarDesdeCSV(csvContent);

        assertNotNull("Juan debería haberse cargado", clienteService.buscar("123"));
        assertEquals("La dirección de Maria debería ser Antigua", "Antigua",
                clienteService.buscar("456").getDireccion());
    }
}
