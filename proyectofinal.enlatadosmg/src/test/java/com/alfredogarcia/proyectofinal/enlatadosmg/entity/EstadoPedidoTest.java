package com.alfredogarcia.proyectofinal.enlatadosmg.entity;

import org.junit.Test;
import static org.junit.Assert.*;

public class EstadoPedidoTest {

    @Test
    public void testValoresDelEnum() {
        // Valida que el enum EstadoPedido tiene exactamente los valores esperados
        EstadoPedido[] valores = EstadoPedido.values();

        assertEquals("Debería tener exactamente 2 estados posibles", 2, valores.length);
        assertEquals("El primer estado debería ser PENDIENTE", EstadoPedido.PENDIENTE, valores[0]);
        assertEquals("El segundo estado debería ser COMPLETADO", EstadoPedido.COMPLETADO, valores[1]);
    }

    @Test
    public void testComparacionDeValores() {
        // Valida que los valores del enum se comparan correctamente
        EstadoPedido estado = EstadoPedido.PENDIENTE;

        assertEquals("El estado debería ser PENDIENTE", EstadoPedido.PENDIENTE, estado);
        assertNotEquals("El estado no debería ser COMPLETADO", EstadoPedido.COMPLETADO, estado);
    }
}
