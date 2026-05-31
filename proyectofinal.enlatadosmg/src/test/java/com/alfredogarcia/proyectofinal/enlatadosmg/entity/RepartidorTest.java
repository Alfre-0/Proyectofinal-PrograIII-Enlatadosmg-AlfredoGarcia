package com.alfredogarcia.proyectofinal.enlatadosmg.entity;

import org.junit.Test;
import static org.junit.Assert.*;

public class RepartidorTest {

    @Test
    public void testConstructorYGetters() {
        // Valida que el constructor asigne correctamente los campos
        Repartidor r = new Repartidor("1111111111111", "José", "García", "A-1234", "55554444");

        assertEquals("DPI incorrecto", "1111111111111", r.getDpi());
        assertEquals("Nombre incorrecto", "José", r.getNombre());
        assertEquals("Apellidos incorrectos", "García", r.getApellidos());
        assertEquals("Licencia incorrecta", "A-1234", r.getLicencia());
        assertEquals("Teléfono incorrecto", "55554444", r.getTelefono());
    }

    @Test
    public void testSetters() {
        // Valida que los setters actualicen los campos correctamente
        Repartidor r = new Repartidor();
        r.setDpi("999");
        r.setNombre("Mario");
        r.setApellidos("Pérez");
        r.setLicencia("B-5678");
        r.setTelefono("99998888");

        assertEquals("DPI actualizado incorrecto", "999", r.getDpi());
        assertEquals("Nombre actualizado incorrecto", "Mario", r.getNombre());
        assertEquals("Licencia actualizada incorrecta", "B-5678", r.getLicencia());
    }
}
