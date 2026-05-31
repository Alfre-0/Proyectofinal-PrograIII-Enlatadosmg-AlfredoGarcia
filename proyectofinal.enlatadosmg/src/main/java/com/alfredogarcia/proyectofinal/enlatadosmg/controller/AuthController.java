package com.alfredogarcia.proyectofinal.enlatadosmg.controller;

import com.alfredogarcia.proyectofinal.enlatadosmg.dto.LoginRequest;
import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Usuario;
import com.alfredogarcia.proyectofinal.enlatadosmg.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    @Autowired
    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Usuario u = usuarioService.login(req.getId(), req.getContrasena());
        if (u != null) {
            return ResponseEntity.ok(u);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("{\"error\": \"ID o contraseña incorrectos.\"}");
    }

    @GetMapping("/perfil/{id}")
    public ResponseEntity<?> obtenerPerfil(@PathVariable int id) {
        Usuario u = usuarioService.buscarPorId(id);
        if (u != null) {
            return ResponseEntity.ok(u);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\": \"Usuario no encontrado.\"}");
    }
}
