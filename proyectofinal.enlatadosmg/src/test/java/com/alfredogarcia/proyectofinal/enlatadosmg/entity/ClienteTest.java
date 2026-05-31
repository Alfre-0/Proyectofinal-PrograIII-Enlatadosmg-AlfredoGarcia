package com.alfredogarcia.proyectofinal.enlatadosmg.entity;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClienteTest {

    @Test
    public void testConstructorYGetters() {
        // Valida que el constructor con parámetros asigne correctamente los campos
        Cliente c = new Cliente("1111111111111", "Juan", "Pérez", "55554444", "Guatemala");

        assertEquals("DPI incorrecto", "1111111111111", c.getDpi());
        assertEquals("Nombre incorrecto", "Juan", c.getNombre());
        assertEquals("Apellidos incorrectos", "Pérez", c.getApellidos());
        assertEquals("Teléfono incorrecto", "55554444", c.getTelefono());
        assertEquals("Dirección incorrecta", "Guatemala", c.getDireccion());
    }

    @Test
    public void testSetters() {
        // Valida que los setters actualicen los campos correctamente
        Cliente c = new Cliente();
        c.setDpi("222");
        c.setNombre("María");
        c.setApellidos("López");
        c.setTelefono("77778888");
        c.setDireccion("Antigua");

        assertEquals("DPI actualizado incorrecto", "222", c.getDpi());
        assertEquals("Nombre actualizado incorrecto", "María", c.getNombre());
        assertEquals("Apellidos actualizados incorrectos", "López", c.getApellidos());
        assertEquals("Teléfono actualizado incorrecto", "77778888", c.getTelefono());
        assertEquals("Dirección actualizada incorrecta", "Antigua", c.getDireccion());
    }
}
