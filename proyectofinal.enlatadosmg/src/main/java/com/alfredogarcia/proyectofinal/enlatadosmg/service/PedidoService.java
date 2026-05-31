package com.alfredogarcia.proyectofinal.enlatadosmg.service;

import com.alfredogarcia.proyectofinal.enlatadosmg.dto.CrearPedidoRequest;
import com.alfredogarcia.proyectofinal.enlatadosmg.entity.*;
import com.alfredogarcia.proyectofinal.enlatadosmg.structures.ListaCajasPedido;
import com.alfredogarcia.proyectofinal.enlatadosmg.structures.ListaEnlazada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PedidoService {
    private final ListaEnlazada<Pedido> pedidos;
    private int contadorPedidos;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ClienteService clienteService;
    private final RepartidorService repartidorService;
    private final VehiculoService vehiculoService;
    private final AlmacenService almacenService;

    @Autowired
    public PedidoService(ClienteService clienteService, RepartidorService repartidorService,
            VehiculoService vehiculoService, AlmacenService almacenService) {
        this.pedidos = new ListaEnlazada<>();
        this.contadorPedidos = 1;
        this.clienteService = clienteService;
        this.repartidorService = repartidorService;
        this.vehiculoService = vehiculoService;
        this.almacenService = almacenService;
    }

    public Pedido crearPedido(CrearPedidoRequest req) {
        // 1. Validaciones
        if (req.getCantidadCajas() <= 0) {
            throw new RuntimeException("La cantidad de cajas debe ser mayor que cero.");
        }

        Cliente cliente = clienteService.buscar(req.getDpiCliente());
        if (cliente == null) {
            throw new RuntimeException("El cliente con DPI " + req.getDpiCliente() + " no existe.");
        }

        if (almacenService.obtenerCantidad() < req.getCantidadCajas()) {
            throw new RuntimeException("No hay suficientes cajas en el almacén. Disponibles: "
                    + almacenService.obtenerCantidad());
        }

        if (repartidorService.obtenerTodos().isEmpty()) {
            throw new RuntimeException("No hay repartidores disponibles en la cola.");
        }

        if (vehiculoService.obtenerTodos().isEmpty()) {
            throw new RuntimeException("No hay vehículos disponibles en la cola.");
        }

        // 2. Extraer cajas (desapilar)
        ListaCajasPedido cajasPedido = new ListaCajasPedido();
        for (int i = 0; i < req.getCantidadCajas(); i++) {
            Caja c = almacenService.desapilarCaja();
            cajasPedido.agregar(c);
        }

        // 3. Desencolar repartidor y vehículo
        Repartidor repartidor = repartidorService.desencolar();
        Vehiculo vehiculo = vehiculoService.desencolar();

        // 4. Crear el pedido
        String fecha = LocalDateTime.now().format(FORMATTER);
        Pedido nuevoPedido = new Pedido(
                contadorPedidos++,
                req.getDepartamentoOrigen(),
                req.getDepartamentoDestino(),
                fecha,
                cliente,
                repartidor,
                vehiculo,
                cajasPedido,
                req.getCantidadCajas(),
                EstadoPedido.PENDIENTE);

        // 5. Guardar pedido en la lista enlazada
        pedidos.insertarFinal(nuevoPedido);

        return nuevoPedido;
    }

    public Pedido completarPedido(int numeroPedido) {
        Pedido pedido = buscarPorNumero(numeroPedido);
        if (pedido == null) {
            throw new RuntimeException("El pedido número " + numeroPedido + " no existe.");
        }

        if (pedido.getEstado() == EstadoPedido.COMPLETADO) {
            throw new RuntimeException("El pedido ya se encuentra COMPLETADO.");
        }

        // Cambiar estado
        pedido.setEstado(EstadoPedido.COMPLETADO);

        // Reinsertar repartidor y vehículo a sus colas
        repartidorService.reencolar(pedido.getRepartidor());
        vehiculoService.reencolar(pedido.getVehiculo());

        return pedido;
    }

    public Pedido buscarPorNumero(int numero) {
        return pedidos.buscar(p -> p.getNumeroPedido() == numero);
    }

    public List<Pedido> obtenerTodos() {
        return pedidos.obtenerTodos();
    }
}
