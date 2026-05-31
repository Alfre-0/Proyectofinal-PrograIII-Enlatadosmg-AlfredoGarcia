package com.alfredogarcia.proyectofinal.enlatadosmg.config;

import com.alfredogarcia.proyectofinal.enlatadosmg.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final UsuarioService usuarioService;

    @Autowired
    public AuthenticationInterceptor(@Lazy UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        // Permitir solicitudes de tipo OPTIONS para que el navegador pueda realizar la verificación previa de CORS
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String uri = request.getRequestURI();

        // Solo proteger endpoints de la API /api/**, excepto /api/auth/**
        if (uri.startsWith("/api/auth")) {
            return true;
        }

        // Si no es un endpoint de API, permitir (archivos estáticos, etc.)
        if (!uri.startsWith("/api/")) {
            return true;
        }

        String userIdHeader = request.getHeader("X-Usuario-Id");
        if (userIdHeader == null || userIdHeader.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"No autorizado. Falta cabecera X-Usuario-Id.\"}");
            return false;
        }

        try {
            int id = Integer.parseInt(userIdHeader);
            if (usuarioService.buscarPorId(id) == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"error\": \"Usuario no encontrado o inexistente.\"}");
                return false;
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"ID de usuario inválido en cabecera.\"}");
            return false;
        }

        return true;
    }
}
