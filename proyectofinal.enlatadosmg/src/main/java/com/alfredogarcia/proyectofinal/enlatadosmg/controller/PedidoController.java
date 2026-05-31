package com.alfredogarcia.proyectofinal.enlatadosmg.controller;

import com.alfredogarcia.proyectofinal.enlatadosmg.dto.CrearPedidoRequest;
import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Caja;
import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Pedido;
import com.alfredogarcia.proyectofinal.enlatadosmg.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Pedido> obtenerTodos() {
        return pedidoService.obtenerTodos();
    }

    @GetMapping("/{numero}")
    public ResponseEntity<?> obtenerPorNumero(@PathVariable int numero) {
        Pedido p = pedidoService.buscarPorNumero(numero);
        if (p != null) {
            return ResponseEntity.ok(p);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody CrearPedidoRequest req) {
        try {
            Pedido creado = pedidoService.crearPedido(req);
            return ResponseEntity.ok(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{numero}/completar")
    public ResponseEntity<?> completarPedido(@PathVariable int numero) {
        try {
            Pedido completado = pedidoService.completarPedido(numero);
            return ResponseEntity.ok(completado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{numero}/cajas")
    public ResponseEntity<?> obtenerCajasPedido(@PathVariable int numero) {
        Pedido p = pedidoService.buscarPorNumero(numero);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        List<Caja> cajas = p.getCajasProducto().obtenerTodas();
        return ResponseEntity.ok(cajas);
    }
}
