package com.alfredogarcia.proyectofinal.enlatadosmg.service;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Repartidor;
import com.alfredogarcia.proyectofinal.enlatadosmg.structures.Cola;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RepartidorService {
    private final Cola<Repartidor> colaRepartidores;
    private final CsvService csvService;

    @Autowired
    public RepartidorService(CsvService csvService) {
        this.csvService = csvService;
        this.colaRepartidores = new Cola<>();
    }

    public Repartidor agregar(Repartidor r) {
        if (r.getDpi() == null || r.getDpi().trim().isEmpty()) {
            throw new RuntimeException("El DPI no puede ser nulo o vacío.");
        }
        if (buscarPorDpi(r.getDpi()) != null) {
            throw new RuntimeException("El repartidor con DPI " + r.getDpi() + " ya existe.");
        }
        colaRepartidores.encolar(r);
        return r;
    }

    public Repartidor buscarPorDpi(String dpi) {
        return colaRepartidores.buscar(r -> r.getDpi().equals(dpi));
    }

    public Repartidor desencolar() {
        if (colaRepartidores.estaVacia()) {
            return null;
        }
        return colaRepartidores.desencolar();
    }

    public void reencolar(Repartidor r) {
        colaRepartidores.encolar(r);
    }

    public List<Repartidor> obtenerTodos() {
        return colaRepartidores.obtenerTodos();
    }

    public void eliminar(String dpi) {
        boolean eliminado = colaRepartidores.eliminarNodo(r -> r.getDpi().equals(dpi));
        if (!eliminado) {
            throw new RuntimeException("El repartidor con DPI " + dpi + " no existe.");
        }
    }

    public Repartidor modificar(String dpi, Repartidor repartidorActualizado) {
        Repartidor r = buscarPorDpi(dpi);
        if (r == null) {
            throw new RuntimeException("El repartidor con DPI " + dpi + " no existe.");
        }
        r.setNombre(repartidorActualizado.getNombre());
        r.setApellidos(repartidorActualizado.getApellidos());
        r.setLicencia(repartidorActualizado.getLicencia());
        r.setTelefono(repartidorActualizado.getTelefono());
        return r;
    }

    public void cargarDesdeCSV(String contenido) {
        List<String[]> lineas = csvService.parsearLineas(contenido);
        for (String[] partes : lineas) {
            if (partes.length < 5) continue;
            String dpi = partes[0];
            String nombre = partes[1];
            String apellido = partes[2];
            String licencia = partes[3];
            String telefono = partes[4];

            Repartidor r = new Repartidor(dpi, nombre, apellido, licencia, telefono);
            if (buscarPorDpi(dpi) == null) {
                colaRepartidores.encolar(r);
            }
        }
    }
}
