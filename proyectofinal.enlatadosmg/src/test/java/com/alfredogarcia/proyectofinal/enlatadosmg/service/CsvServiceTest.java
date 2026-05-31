package com.alfredogarcia.proyectofinal.enlatadosmg.service;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class CsvServiceTest {

    private CsvService csvService;

    @Before
    public void setUp() {
        // Inicializa el servicio antes de cada prueba
        csvService = new CsvService();
    }

    @Test
    public void testParsearLineasExitoso() {
        // Valida que se parseen correctamente las líneas saltándose la cabecera (primera fila) y usando separador ";"
        String csvContent = "DPI;Nombre;Apellido;Telefono\n" +
                "123;Juan;Perez;5555\n" +
                "456;Maria;Gomez;7777\n";

        List<String[]> resultado = csvService.parsearLineas(csvContent);

        assertEquals("Deberían retornar 2 filas de datos (excluyendo cabecera)", 2, resultado.size());

        String[] fila1 = resultado.get(0);
        assertEquals("Fila 1 columna 0 debería ser 123", "123", fila1[0]);
        assertEquals("Fila 1 columna 1 debería ser Juan", "Juan", fila1[1]);
        assertEquals("Fila 1 columna 2 debería ser Perez", "Perez", fila1[2]);
        assertEquals("Fila 1 columna 3 debería ser 5555", "5555", fila1[3]);

        String[] fila2 = resultado.get(1);
        assertEquals("Fila 2 columna 0 debería ser 456", "456", fila2[0]);
    }

    @Test
    public void testParsearContenidoVacio() {
        // Valida que retorne una lista vacía si el contenido es nulo o vacío
        List<String[]> resultadoNulo = csvService.parsearLineas(null);
        assertTrue("Debería retornar una lista vacía para entrada nula", resultadoNulo.isEmpty());

        List<String[]> resultadoVacio = csvService.parsearLineas("   ");
        assertTrue("Debería retornar una lista vacía para entrada en blanco", resultadoVacio.isEmpty());
    }
}
