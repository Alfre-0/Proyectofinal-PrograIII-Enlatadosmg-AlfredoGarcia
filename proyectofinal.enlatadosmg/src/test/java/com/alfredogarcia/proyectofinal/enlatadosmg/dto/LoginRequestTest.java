package com.alfredogarcia.proyectofinal.enlatadosmg.dto;

import org.junit.Test;
import static org.junit.Assert.*;

public class LoginRequestTest {

    @Test
    public void testConstructorYGetters() {
        // Valida que el constructor con parámetros asigne correctamente los campos del DTO
        LoginRequest req = new LoginRequest(1, "password123");

        assertEquals("El ID debería ser 1", 1, req.getId());
        assertEquals("La contraseña debería coincidir", "password123", req.getContrasena());
    }

    @Test
    public void testSetters() {
        // Valida que los setters actualicen los campos correctamente
        LoginRequest req = new LoginRequest();
        req.setId(5);
        req.setContrasena("nuevaClave");

        assertEquals("El ID actualizado es incorrecto", 5, req.getId());
        assertEquals("La contraseña actualizada es incorrecta", "nuevaClave", req.getContrasena());
    }
}
