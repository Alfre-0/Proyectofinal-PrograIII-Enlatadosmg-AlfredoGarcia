package com.alfredogarcia.proyectofinal.enlatadosmg.controller;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Caja;
import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Pedido;
import com.alfredogarcia.proyectofinal.enlatadosmg.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final DotService dotService;
    private final UsuarioService usuarioService;
    private final AlmacenService almacenService;
    private final ClienteService clienteService;
    private final RepartidorService repartidorService;
    private final VehiculoService vehiculoService;
    private final PedidoService pedidoService;

    @Autowired
    public ReporteController(DotService dotService, UsuarioService usuarioService,
                             AlmacenService almacenService, ClienteService clienteService,
                             RepartidorService repartidorService, VehiculoService vehiculoService,
                             PedidoService pedidoService) {
        this.dotService = dotService;
        this.usuarioService = usuarioService;
        this.almacenService = almacenService;
        this.clienteService = clienteService;
        this.repartidorService = repartidorService;
        this.vehiculoService = vehiculoService;
        this.pedidoService = pedidoService;
    }

    private ResponseEntity<String> responderDot(String dotContent) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("text/plain;charset=UTF-8"));
        return new ResponseEntity<>(dotContent, headers, HttpStatus.OK);
    }

    @GetMapping("/usuarios")
    public ResponseEntity<String> reporteUsuarios() {
        String dot = dotService.generarDotUsuarios(usuarioService.obtenerTodos());
        return responderDot(dot);
    }

    @GetMapping("/almacen")
    public ResponseEntity<String> reporteAlmacen() {
        String dot = dotService.generarDotAlmacen(almacenService.obtenerTodas());
        return responderDot(dot);
    }

    @GetMapping("/clientes")
    public ResponseEntity<String> reporteClientes() {
        String dot = dotService.generarDotClientes(clienteService.obtenerArbol());
        return responderDot(dot);
    }

    @GetMapping("/repartidores")
    public ResponseEntity<String> reporteRepartidores() {
        String dot = dotService.generarDotRepartidores(repartidorService.obtenerTodos());
        return responderDot(dot);
    }

    @GetMapping("/vehiculos")
    public ResponseEntity<String> reporteVehiculos() {
        String dot = dotService.generarDotVehiculos(vehiculoService.obtenerTodos());
        return responderDot(dot);
    }

    @GetMapping("/pedidos")
    public ResponseEntity<String> reportePedidos() {
        String dot = dotService.generarDotPedidos(pedidoService.obtenerTodos());
        return responderDot(dot);
    }

    @GetMapping("/pedidos/{numero}/cajas")
    public ResponseEntity<String> reporteCajasPedido(@PathVariable int numero) {
        Pedido p = pedidoService.buscarPorNumero(numero);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        List<Caja> cajas = p.getCajasProducto().obtenerTodas();
        String dot = dotService.generarDotCajasPedido(cajas, numero);
        return responderDot(dot);
    }
}
