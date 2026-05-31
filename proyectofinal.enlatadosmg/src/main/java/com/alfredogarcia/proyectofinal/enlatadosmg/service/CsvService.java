package com.alfredogarcia.proyectofinal.enlatadosmg.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    public List<String[]> parsearLineas(String contenido) {
        List<String[]> resultado = new ArrayList<>();
        if (contenido == null || contenido.trim().isEmpty()) {
            return resultado;
        }

        String[] lineas = contenido.split("\\r?\\n");
        // Omitimos la primera línea (cabecera)
        for (int i = 1; i < lineas.length; i++) {
            String linea = lineas[i].trim();
            if (linea.isEmpty()) {
                continue;
            }
            String[] partes = linea.split(";");
            for (int j = 0; j < partes.length; j++) {
                partes[j] = partes[j].trim();
            }
            resultado.add(partes);
        }
        return resultado;
    }
}
