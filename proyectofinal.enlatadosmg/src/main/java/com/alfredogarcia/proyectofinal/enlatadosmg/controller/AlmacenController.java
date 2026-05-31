package com.alfredogarcia.proyectofinal.enlatadosmg.controller;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Caja;
import com.alfredogarcia.proyectofinal.enlatadosmg.service.AlmacenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/almacen")
public class AlmacenController {

    private final AlmacenService almacenService;

    @Autowired
    public AlmacenController(AlmacenService almacenService) {
        this.almacenService = almacenService;
    }

    @PostMapping("/generar/{cantidad}")
    public ResponseEntity<?> generarCajas(@PathVariable int cantidad) {
        try {
            almacenService.generarCajas(cantidad);
            return ResponseEntity.ok(Map.of("mensaje", "Se han generado " + cantidad + " cajas en la pila del almacén."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public List<Caja> obtenerTodas() {
        return almacenService.obtenerTodas();
    }

    @GetMapping("/cantidad")
    public ResponseEntity<?> obtenerCantidad() {
        return ResponseEntity.ok(Map.of("cantidad", almacenService.obtenerCantidad()));
    }

    @PostMapping("/push")
    public ResponseEntity<?> apilarCaja() {
        try {
            Caja c = almacenService.apilarCaja();
            return ResponseEntity.ok(c);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/pop")
    public ResponseEntity<?> desapilarCaja() {
        try {
            Caja c = almacenService.desapilarCaja();
            return ResponseEntity.ok(c);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
