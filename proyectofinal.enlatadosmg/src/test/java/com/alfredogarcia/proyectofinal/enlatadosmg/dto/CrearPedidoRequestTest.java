package com.alfredogarcia.proyectofinal.enlatadosmg.dto;

import org.junit.Test;
import static org.junit.Assert.*;

public class CrearPedidoRequestTest {

    @Test
    public void testGettersSettersYConstructores() {
        // Valida que el constructor vacío y con parámetros, así como getters y setters del DTO funcionen
        CrearPedidoRequest request = new CrearPedidoRequest("123456", "Guatemala", "Escuintla", 10);

        assertEquals("DPI del cliente debería coincidir", "123456", request.getDpiCliente());
        assertEquals("Origen debería coincidir", "Guatemala", request.getDepartamentoOrigen());
        assertEquals("Destino debería coincidir", "Escuintla", request.getDepartamentoDestino());
        assertEquals("Cantidad de cajas debería coincidir", 10, request.getCantidadCajas());

        request.setDpiCliente("654321");
        request.setDepartamentoOrigen("Retalhuleu");
        request.setDepartamentoDestino("Petén");
        request.setCantidadCajas(2);

        assertEquals("DPI actualizado incorrecto", "654321", request.getDpiCliente());
        assertEquals("Origen actualizado incorrecto", "Retalhuleu", request.getDepartamentoOrigen());
        assertEquals("Destino actualizado incorrecto", "Petén", request.getDepartamentoDestino());
        assertEquals("Cantidad de cajas actualizada incorrecta", 2, request.getCantidadCajas());
    }
}
