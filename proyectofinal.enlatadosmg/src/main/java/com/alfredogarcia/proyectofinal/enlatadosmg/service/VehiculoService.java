package com.alfredogarcia.proyectofinal.enlatadosmg.service;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Vehiculo;
import com.alfredogarcia.proyectofinal.enlatadosmg.structures.Cola;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VehiculoService {
    private final Cola<Vehiculo> colaVehiculos;
    private final CsvService csvService;

    @Autowired
    public VehiculoService(CsvService csvService) {
        this.csvService = csvService;
        this.colaVehiculos = new Cola<>();
    }

    public Vehiculo agregar(Vehiculo v) {
        if (v.getPlaca() == null || v.getPlaca().trim().isEmpty()) {
            throw new RuntimeException("La placa no puede ser nula o vacía.");
        }
        if (buscarPorPlaca(v.getPlaca()) != null) {
            throw new RuntimeException("El vehículo con placa " + v.getPlaca() + " ya existe.");
        }
        colaVehiculos.encolar(v);
        return v;
    }

    public Vehiculo buscarPorPlaca(String placa) {
        return colaVehiculos.buscar(v -> v.getPlaca().equals(placa));
    }

    public Vehiculo desencolar() {
        if (colaVehiculos.estaVacia()) {
            return null;
        }
        return colaVehiculos.desencolar();
    }

    public void reencolar(Vehiculo v) {
        colaVehiculos.encolar(v);
    }

    public List<Vehiculo> obtenerTodos() {
        return colaVehiculos.obtenerTodos();
    }

    public void eliminar(String placa) {
        boolean eliminado = colaVehiculos.eliminarNodo(v -> v.getPlaca().equals(placa));
        if (!eliminado) {
            throw new RuntimeException("El vehículo con placa " + placa + " no existe.");
        }
    }

    public Vehiculo modificar(String placa, Vehiculo vehiculoActualizado) {
        Vehiculo v = buscarPorPlaca(placa);
        if (v == null) {
            throw new RuntimeException("El vehículo con placa " + placa + " no existe.");
        }
        v.setMarca(vehiculoActualizado.getMarca());
        v.setModelo(vehiculoActualizado.getModelo());
        v.setColor(vehiculoActualizado.getColor());
        v.setAnio(vehiculoActualizado.getAnio());
        v.setTipoTransmision(vehiculoActualizado.getTipoTransmision());
        return v;
    }

    public void cargarDesdeCSV(String contenido) {
        List<String[]> lineas = csvService.parsearLineas(contenido);
        for (String[] partes : lineas) {
            if (partes.length < 6) continue;
            try {
                String placa = partes[0];
                String marca = partes[1];
                String modelo = partes[2];
                String color = partes[3];
                int anio = Integer.parseInt(partes[4]);
                String tipoTransmision = partes[5];

                Vehiculo v = new Vehiculo(placa, marca, modelo, color, anio, tipoTransmision);
                if (buscarPorPlaca(placa) == null) {
                    colaVehiculos.encolar(v);
                }
            } catch (NumberFormatException e) {
                // Saltar registro
            }
        }
    }
}
