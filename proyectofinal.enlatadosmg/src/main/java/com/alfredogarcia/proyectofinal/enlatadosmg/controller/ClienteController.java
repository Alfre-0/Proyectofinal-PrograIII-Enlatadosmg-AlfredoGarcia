package com.alfredogarcia.proyectofinal.enlatadosmg.controller;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Cliente;
import com.alfredogarcia.proyectofinal.enlatadosmg.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<Cliente> obtenerTodos() {
        return clienteService.listarTodos();
    }

    @GetMapping("/{dpi}")
    public ResponseEntity<?> obtenerPorDpi(@PathVariable String dpi) {
        Cliente c = clienteService.buscar(dpi);
        if (c != null) {
            return ResponseEntity.ok(c);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crearCliente(@RequestBody Cliente c) {
        try {
            Cliente creado = clienteService.insertar(c);
            return ResponseEntity.ok(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{dpi}")
    public ResponseEntity<?> modificarCliente(@PathVariable String dpi, @RequestBody Cliente c) {
        try {
            Cliente modificado = clienteService.modificar(dpi, c);
            return ResponseEntity.ok(modificado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{dpi}")
    public ResponseEntity<?> eliminarCliente(@PathVariable String dpi) {
        try {
            clienteService.eliminar(dpi);
            return ResponseEntity.ok("{\"mensaje\": \"Cliente eliminado correctamente.\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/carga")
    public ResponseEntity<?> cargarCSV(@RequestBody String csvContenido) {
        try {
            clienteService.cargarDesdeCSV(csvContenido);
            return ResponseEntity.ok("{\"mensaje\": \"Carga masiva de clientes completada.\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
