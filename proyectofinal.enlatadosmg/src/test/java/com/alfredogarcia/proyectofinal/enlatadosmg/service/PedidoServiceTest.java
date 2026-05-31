package com.alfredogarcia.proyectofinal.enlatadosmg.service;

import com.alfredogarcia.proyectofinal.enlatadosmg.dto.CrearPedidoRequest;
import com.alfredogarcia.proyectofinal.enlatadosmg.entity.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PedidoServiceTest {

    private PedidoService pedidoService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private RepartidorService repartidorService;

    @Mock
    private VehiculoService vehiculoService;

    @Mock
    private AlmacenService almacenService;

    @Before
    public void setUp() {
        // Inicializa las anotaciones de Mockito antes de cada prueba
        MockitoAnnotations.openMocks(this);
        // Construye el servicio inyectando los mocks creados
        pedidoService = new PedidoService(clienteService, repartidorService, vehiculoService, almacenService);
    }

    @Test
    public void testCrearPedidoExitoso() {
        // Preparación de datos simulados
        String dpi = "1234567890101";
        Cliente clienteSimulado = new Cliente(dpi, "Ana", "Rodríguez", "55551111", "Guatemala");
        Repartidor repartidorSimulado = new Repartidor("4567890123034", "Jorge", "Ortega", "A", "55554444");
        Vehiculo vehiculoSimulado = new Vehiculo("P100AAA", "Toyota", "Hilux", "Blanco", 2020, "Manual");
        Caja cajaSimulada = new Caja(1, "2026-05-24 12:00:00");

        // Configuración de los comportamientos (Mocks)
        when(clienteService.buscar(dpi)).thenReturn(clienteSimulado);
        when(almacenService.obtenerCantidad()).thenReturn(10); // Suficientes cajas
        when(almacenService.desapilarCaja()).thenReturn(cajaSimulada);
        when(repartidorService.obtenerTodos()).thenReturn(List.of(repartidorSimulado));
        when(repartidorService.desencolar()).thenReturn(repartidorSimulado);
        when(vehiculoService.obtenerTodos()).thenReturn(List.of(vehiculoSimulado));
        when(vehiculoService.desencolar()).thenReturn(vehiculoSimulado);

        // Ejecución
        CrearPedidoRequest req = new CrearPedidoRequest(dpi, "Guatemala", "Sacatepéquez", 1);
        Pedido pedidoCreado = pedidoService.crearPedido(req);

        // Verificaciones
        assertNotNull("El pedido no debería ser nulo", pedidoCreado);
        assertEquals("El estado inicial debería ser PENDIENTE", EstadoPedido.PENDIENTE, pedidoCreado.getEstado());
        assertEquals("El cliente debería ser Ana", "Ana", pedidoCreado.getCliente().getNombre());
        assertEquals("Debería tener 1 caja asignada", 1, pedidoCreado.getNumeroCajas());

        // Verificar que se invocaron los métodos esperados
        verify(clienteService).buscar(dpi);
        verify(almacenService).desapilarCaja();
        verify(repartidorService).desencolar();
        verify(vehiculoService).desencolar();
    }

    @Test
    public void testCrearPedidoFallaClienteNoExiste() {
        String dpiInexistente = "0000000000000";
        when(clienteService.buscar(dpiInexistente)).thenReturn(null);

        CrearPedidoRequest req = new CrearPedidoRequest(dpiInexistente, "Guatemala", "Sacatepéquez", 5);

        try {
            pedidoService.crearPedido(req);
            fail("Debería haber lanzado una RuntimeException por cliente no existente.");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("no existe"));
        }
    }

    @Test
    public void testCrearPedidoFallaCajasInsuficientes() {
        String dpi = "1234567890101";
        Cliente clienteSimulado = new Cliente(dpi, "Ana", "Rodríguez", "55551111", "Guatemala");

        when(clienteService.buscar(dpi)).thenReturn(clienteSimulado);
        when(almacenService.obtenerCantidad()).thenReturn(2); // Cantidad en almacén insuficiente para el pedido

        CrearPedidoRequest req = new CrearPedidoRequest(dpi, "Guatemala", "Sacatepéquez", 5);

        try {
            pedidoService.crearPedido(req);
            fail("Debería lanzar error por falta de existencias en el almacén.");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Disponibles: 2"));
        }
    }

    @Test
    public void testCompletarPedidoExitoso() {
        // Simular un pedido ya creado
        Cliente cliente = new Cliente("123", "Ana", "R", "1", "Gua");
        Repartidor rep = new Repartidor("456", "Jorge", "O", "A", "2");
        Vehiculo veh = new Vehiculo("P100AAA", "Toyota", "Hilux", "Blanco", 2020, "Manual");


        // Agregamos el pedido al listado interno del servicio.
        // Como 'pedidos' es una estructura interna 'ListaEnlazada', podemos usar buscarPorNumero si el pedido ya está en la lista.
        // No tiene un método público directo para agregar sin crear, por lo tanto usamos Mockito de forma inteligente o lo creamos.
        // Como 'crearPedido' es lo que inserta el pedido en 'pedidos', llamamos primero a crearPedido para preparar la prueba de completar.
        
        when(clienteService.buscar(anyString())).thenReturn(cliente);
        when(almacenService.obtenerCantidad()).thenReturn(10);
        when(repartidorService.obtenerTodos()).thenReturn(List.of(rep));
        when(repartidorService.desencolar()).thenReturn(rep);
        when(vehiculoService.obtenerTodos()).thenReturn(List.of(veh));
        when(vehiculoService.desencolar()).thenReturn(veh);

        CrearPedidoRequest req = new CrearPedidoRequest("123", "Guatemala", "Sacatepéquez", 1);
        Pedido creado = pedidoService.crearPedido(req);

        // Completar el pedido creado
        Pedido completado = pedidoService.completarPedido(creado.getNumeroPedido());

        assertEquals("El estado final debería ser COMPLETADO", EstadoPedido.COMPLETADO, completado.getEstado());
        // Se debe verificar que el repartidor y el vehículo regresan a las colas correspondientes
        verify(repartidorService).reencolar(rep);
        verify(vehiculoService).reencolar(veh);
    }
}
