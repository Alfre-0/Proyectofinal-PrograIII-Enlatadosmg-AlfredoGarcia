package com.alfredogarcia.proyectofinal.enlatadosmg.controller;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Repartidor;
import com.alfredogarcia.proyectofinal.enlatadosmg.service.RepartidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/repartidores")
public class RepartidorController {

    private final RepartidorService repartidorService;

    @Autowired
    public RepartidorController(RepartidorService repartidorService) {
        this.repartidorService = repartidorService;
    }

    @GetMapping
    public List<Repartidor> obtenerTodos() {
        return repartidorService.obtenerTodos();
    }

    @GetMapping("/{dpi}")
    public ResponseEntity<?> obtenerPorDpi(@PathVariable String dpi) {
        Repartidor r = repartidorService.buscarPorDpi(dpi);
        if (r != null) {
            return ResponseEntity.ok(r);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crearRepartidor(@RequestBody Repartidor r) {
        try {
            Repartidor creado = repartidorService.agregar(r);
            return ResponseEntity.ok(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{dpi}")
    public ResponseEntity<?> modificarRepartidor(@PathVariable String dpi, @RequestBody Repartidor r) {
        try {
            Repartidor modificado = repartidorService.modificar(dpi, r);
            return ResponseEntity.ok(modificado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{dpi}")
    public ResponseEntity<?> eliminarRepartidor(@PathVariable String dpi) {
        try {
            repartidorService.eliminar(dpi);
            return ResponseEntity.ok("{\"mensaje\": \"Repartidor eliminado de la cola.\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/carga")
    public ResponseEntity<?> cargarCSV(@RequestBody String csvContenido) {
        try {
            repartidorService.cargarDesdeCSV(csvContenido);
            return ResponseEntity.ok("{\"mensaje\": \"Carga masiva de repartidores completada.\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
