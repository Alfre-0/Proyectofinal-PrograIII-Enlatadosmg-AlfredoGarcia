package com.alfredogarcia.proyectofinal.enlatadosmg.entity;

import org.junit.Test;
import static org.junit.Assert.*;

public class UsuarioTest {

    @Test
    public void testConstructorYGetters() {
        // Valida que el constructor con parámetros asigne correctamente todos los campos
        Usuario u = new Usuario(1, "Admin", "General", "1234");

        assertEquals("ID incorrecto", 1, u.getId());
        assertEquals("Nombre incorrecto", "Admin", u.getNombre());
        assertEquals("Apellidos incorrectos", "General", u.getApellidos());
        assertEquals("Contraseña incorrecta", "1234", u.getContrasena());
    }

    @Test
    public void testSetters() {
        // Valida que los setters actualicen los campos correctamente
        Usuario u = new Usuario();
        u.setId(5);
        u.setNombre("Pedro");
        u.setApellidos("Gómez");
        u.setContrasena("pass999");

        assertEquals("ID actualizado incorrecto", 5, u.getId());
        assertEquals("Nombre actualizado incorrecto", "Pedro", u.getNombre());
        assertEquals("Contraseña actualizada incorrecta", "pass999", u.getContrasena());
    }
}
