package com.alfredogarcia.proyectofinal.enlatadosmg.service;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Caja;
import com.alfredogarcia.proyectofinal.enlatadosmg.structures.Pila;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AlmacenService {
    private final Pila<Caja> almacen;
    private int contadorCorrelativo;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AlmacenService() {
        this.almacen = new Pila<>();
        this.contadorCorrelativo = 1;
    }

    public void generarCajas(int cantidad) {
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor que cero.");
        }
        for (int i = 0; i < cantidad; i++) {
            apilarCaja();
        }
    }

    public Caja apilarCaja() {
        String fecha = LocalDateTime.now().format(FORMATTER);
        Caja nueva = new Caja(contadorCorrelativo++, fecha);
        almacen.apilar(nueva);
        return nueva;
    }

    public Caja desapilarCaja() {
        if (almacen.estaVacia()) {
            throw new RuntimeException("No hay cajas en el almacén.");
        }
        return almacen.desapilar();
    }

    public int obtenerCantidad() {
        return almacen.obtenerTamanio();
    }

    public List<Caja> obtenerTodas() {
        return almacen.obtenerTodos();
    }
}
