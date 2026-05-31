package com.alfredogarcia.proyectofinal.enlatadosmg.entity;

import org.junit.Test;
import static org.junit.Assert.*;

public class VehiculoTest {

    @Test
    public void testConstructorYGetters() {
        // Valida que el constructor con parámetros asigne correctamente todos los campos
        Vehiculo v = new Vehiculo("P123ABC", "Toyota", "Hilux", "Blanco", 2020, "Manual");

        assertEquals("Placa incorrecta", "P123ABC", v.getPlaca());
        assertEquals("Marca incorrecta", "Toyota", v.getMarca());
        assertEquals("Modelo incorrecto", "Hilux", v.getModelo());
        assertEquals("Color incorrecto", "Blanco", v.getColor());
        assertEquals("Año incorrecto", 2020, v.getAnio());
        assertEquals("Tipo de transmisión incorrecto", "Manual", v.getTipoTransmision());
    }

    @Test
    public void testSetters() {
        // Valida que los setters actualicen los campos correctamente
        Vehiculo v = new Vehiculo();
        v.setPlaca("Q999ZZZ");
        v.setMarca("Honda");
        v.setModelo("Civic");
        v.setColor("Azul");
        v.setAnio(2022);
        v.setTipoTransmision("Automático");

        assertEquals("Placa actualizada incorrecta", "Q999ZZZ", v.getPlaca());
        assertEquals("Marca actualizada incorrecta", "Honda", v.getMarca());
        assertEquals("Color actualizado incorrecto", "Azul", v.getColor());
        assertEquals("Año actualizado incorrecto", 2022, v.getAnio());
    }
}
