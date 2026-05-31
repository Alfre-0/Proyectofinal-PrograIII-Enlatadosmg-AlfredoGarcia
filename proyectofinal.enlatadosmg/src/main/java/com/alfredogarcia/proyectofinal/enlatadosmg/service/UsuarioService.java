package com.alfredogarcia.proyectofinal.enlatadosmg.service;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Usuario;
import com.alfredogarcia.proyectofinal.enlatadosmg.structures.ListaEnlazada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {
    private final ListaEnlazada<Usuario> usuarios;
    private final CsvService csvService;

    @Autowired
    public UsuarioService(CsvService csvService) {
        this.csvService = csvService;
        this.usuarios = new ListaEnlazada<>();
        // Precarga de usuarios iniciales en el sistema
        this.usuarios.insertarFinal(new Usuario(1, "Admin", "General", "1234"));
        this.usuarios.insertarFinal(new Usuario(2, "Alfredo", "Test", "1234"));
        this.usuarios.insertarFinal(new Usuario(3, "José", "Probando", "1234"));
    }

    public Usuario crearUsuario(Usuario u) {
        if (buscarPorId(u.getId()) != null) {
            throw new RuntimeException("El ID de usuario ya existe: " + u.getId());
        }
        usuarios.insertarFinal(u);
        return u;
    }

    public Usuario buscarPorId(int id) {
        return usuarios.buscar(u -> u.getId() == id);
    }

    public Usuario login(int id, String contrasena) {
        Usuario u = buscarPorId(id);
        if (u != null && u.getContrasena().equals(contrasena)) {
            return u;
        }
        return null;
    }

    public List<Usuario> obtenerTodos() {
        return usuarios.obtenerTodos();
    }

    public void cargarDesdeCSV(String contenido) {
        List<String[]> lineas = csvService.parsearLineas(contenido);
        for (String[] partes : lineas) {
            if (partes.length < 4) continue;
            try {
                int id = Integer.parseInt(partes[0]);
                String nombre = partes[1];
                String apellido = partes[2];
                String contrasena = partes[3];
                
                Usuario u = new Usuario(id, nombre, apellido, contrasena);
                if (buscarPorId(id) == null) {
                    usuarios.insertarFinal(u);
                }
            } catch (NumberFormatException e) {
                // Saltar registros inválidos
            }
        }
    }
}
