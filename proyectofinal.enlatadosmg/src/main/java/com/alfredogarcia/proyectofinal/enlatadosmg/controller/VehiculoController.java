package com.alfredogarcia.proyectofinal.enlatadosmg.controller;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Vehiculo;
import com.alfredogarcia.proyectofinal.enlatadosmg.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @Autowired
    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public List<Vehiculo> obtenerTodos() {
        return vehiculoService.obtenerTodos();
    }

    @GetMapping("/{placa}")
    public ResponseEntity<?> obtenerPorPlaca(@PathVariable String placa) {
        Vehiculo v = vehiculoService.buscarPorPlaca(placa);
        if (v != null) {
            return ResponseEntity.ok(v);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crearVehiculo(@RequestBody Vehiculo v) {
        try {
            Vehiculo creado = vehiculoService.agregar(v);
            return ResponseEntity.ok(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{placa}")
    public ResponseEntity<?> modificarVehiculo(@PathVariable String placa, @RequestBody Vehiculo v) {
        try {
            Vehiculo modificado = vehiculoService.modificar(placa, v);
            return ResponseEntity.ok(modificado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{placa}")
    public ResponseEntity<?> eliminarVehiculo(@PathVariable String placa) {
        try {
            vehiculoService.eliminar(placa);
            return ResponseEntity.ok("{\"mensaje\": \"Vehículo eliminado de la cola.\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/carga")
    public ResponseEntity<?> cargarCSV(@RequestBody String csvContenido) {
        try {
            vehiculoService.cargarDesdeCSV(csvContenido);
            return ResponseEntity.ok("{\"mensaje\": \"Carga masiva de vehículos completada.\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
