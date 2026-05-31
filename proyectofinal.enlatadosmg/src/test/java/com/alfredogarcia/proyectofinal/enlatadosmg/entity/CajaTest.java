package com.alfredogarcia.proyectofinal.enlatadosmg.entity;

import org.junit.Test;
import static org.junit.Assert.*;

public class CajaTest {

    @Test
    public void testGettersSettersYConstructores() {
        // Valida que el constructor y getters/setters de la entidad Caja funcionen
        Caja caja = new Caja(101, "2026-05-24 10:00:00");

        assertEquals("El correlativo debería ser 101", 101, caja.getCorrelativo());
        assertEquals("La fecha de ingreso debería ser 2026-05-24 10:00:00", "2026-05-24 10:00:00", caja.getFechaIngreso());

        caja.setCorrelativo(202);
        caja.setFechaIngreso("2026-05-24 10:05:00");

        assertEquals("El correlativo actualizado es incorrecto", 202, caja.getCorrelativo());
        assertEquals("La fecha de ingreso actualizada es incorrecta", "2026-05-24 10:05:00", caja.getFechaIngreso());
    }
}
