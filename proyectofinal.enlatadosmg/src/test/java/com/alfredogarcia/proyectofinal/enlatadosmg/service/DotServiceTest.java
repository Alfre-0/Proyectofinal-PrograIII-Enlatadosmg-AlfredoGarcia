package com.alfredogarcia.proyectofinal.enlatadosmg.service;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Caja;
import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Usuario;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class DotServiceTest {

    private DotService dotService;

    @Before
    public void setUp() {
        dotService = new DotService();
    }

    @Test
    public void testGenerarDotUsuariosVacio() {
        // Valida que genere la estructura vacía esperada
        String dot = dotService.generarDotUsuarios(new ArrayList<>());
        assertTrue("Debería iniciar con digraph G", dot.contains("digraph G"));
        assertTrue("Debería indicar lista vacía", dot.contains("Lista Vacía"));
    }

    @Test
    public void testGenerarDotUsuariosConDatos() {
        // Valida que genere el DOT con las relaciones entre nodos de usuario
        List<Usuario> listado = new ArrayList<>();
        listado.add(new Usuario(1, "Pedro", "Gomez", "123"));
        listado.add(new Usuario(2, "Maria", "Lopez", "456"));

        String dot = dotService.generarDotUsuarios(listado);
        assertTrue("Debería declarar el nodo u1", dot.contains("u1 [label="));
        assertTrue("Debería declarar el nodo u2", dot.contains("u2 [label="));
        assertTrue("Debería tener una transición de u1 a u2", dot.contains("u1 -> u2;"));
    }

    @Test
    public void testGenerarDotAlmacenVacio() {
        // Valida la estructura vacía del almacén en DOT
        String dot = dotService.generarDotAlmacen(null);
        assertTrue("Debería indicar almacén vacío", dot.contains("Almacén Vacío"));
    }

    @Test
    public void testGenerarDotAlmacenConDatos() {
        // Valida que dibuje la pila de cajas en el almacén
        List<Caja> listado = new ArrayList<>();
        listado.add(new Caja(10, "2026-05-24 10:00:00"));
        listado.add(new Caja(11, "2026-05-24 10:01:00"));

        String dot = dotService.generarDotAlmacen(listado);
        assertTrue("Debería incluir la caja 10", dot.contains("c10 [label="));
        assertTrue("Debería incluir la caja 11", dot.contains("c11 [label="));
        assertTrue("Debería enlazar la caja 10 con la 11 con la etiqueta abajo", dot.contains("c10 -> c11 [label=\"abajo\"];"));
    }
}
