package com.alfredogarcia.proyectofinal.enlatadosmg.service;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Cliente;
import com.alfredogarcia.proyectofinal.enlatadosmg.structures.ArbolAVLClientes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClienteService {
    private final ArbolAVLClientes arbol;
    private final CsvService csvService;

    @Autowired
    public ClienteService(CsvService csvService) {
        this.csvService = csvService;
        this.arbol = new ArbolAVLClientes();
    }

    public Cliente insertar(Cliente c) {
        if (c.getDpi() == null || c.getDpi().trim().isEmpty()) {
            throw new RuntimeException("El DPI no puede ser nulo o vacío.");
        }
        if (buscar(c.getDpi()) != null) {
            throw new RuntimeException("El cliente con DPI " + c.getDpi() + " ya existe.");
        }
        arbol.insertar(c);
        return c;
    }

    public Cliente buscar(String dpi) {
        return arbol.buscar(dpi);
    }

    public Cliente modificar(String dpi, Cliente c) {
        Cliente existente = buscar(dpi);
        if (existente == null) {
            throw new RuntimeException("El cliente con DPI " + dpi + " no existe.");
        }
        // Si cambia el DPI, debe validarse que no choque. Pero usualmente modificamos otros campos.
        // Si el DPI es el mismo:
        if (dpi.equals(c.getDpi())) {
            arbol.modificar(dpi, c);
        } else {
            // Si cambia el DPI, eliminamos el anterior e insertamos el nuevo
            if (buscar(c.getDpi()) != null) {
                throw new RuntimeException("El nuevo DPI " + c.getDpi() + " ya está en uso.");
            }
            arbol.eliminar(dpi);
            arbol.insertar(c);
        }
        return c;
    }

    public void eliminar(String dpi) {
        Cliente existente = buscar(dpi);
        if (existente == null) {
            throw new RuntimeException("El cliente con DPI " + dpi + " no existe.");
        }
        arbol.eliminar(dpi);
    }

    public List<Cliente> listarTodos() {
        return arbol.recorridoInorden();
    }

    public ArbolAVLClientes obtenerArbol() {
        return arbol;
    }

    public void cargarDesdeCSV(String contenido) {
        List<String[]> lineas = csvService.parsearLineas(contenido);
        for (String[] partes : lineas) {
            if (partes.length < 4) continue;
            String dpi = partes[0];
            String nombre = partes[1];
            String apellido = partes[2];
            String telefono = partes[3];
            String direccion = (partes.length >= 5) ? partes[4] : "Guatemala"; // Por defecto Guatemala si no viene

            Cliente c = new Cliente(dpi, nombre, apellido, telefono, direccion);
            if (buscar(dpi) == null) {
                arbol.insertar(c);
            }
        }
    }
}
