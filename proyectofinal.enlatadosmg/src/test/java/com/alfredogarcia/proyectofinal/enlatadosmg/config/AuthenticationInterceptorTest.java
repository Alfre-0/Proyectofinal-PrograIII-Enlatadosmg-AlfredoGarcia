package com.alfredogarcia.proyectofinal.enlatadosmg.config;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.Usuario;
import com.alfredogarcia.proyectofinal.enlatadosmg.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("null")
public class AuthenticationInterceptorTest {

    private AuthenticationInterceptor interceptor;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        interceptor = new AuthenticationInterceptor(usuarioService);
    }

    @Test
    public void testPreHandleOptionsMethod() throws Exception {
        // Valida que el método OPTIONS siempre sea permitido por CORS
        when(request.getMethod()).thenReturn("OPTIONS");

        boolean result = interceptor.preHandle(request, response, new Object());

        assertTrue("OPTIONS debería retornar true", result);
        verifyNoInteractions(response, usuarioService);
    }

    @Test
    public void testPreHandleRutaPublicaAuth() throws Exception {
        // Valida que los endpoints de /api/auth sean públicos y retornen true
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/auth/login");

        boolean result = interceptor.preHandle(request, response, new Object());

        assertTrue("Rutas de auth deberían ser públicas", result);
        verifyNoInteractions(response, usuarioService);
    }

    @Test
    public void testPreHandleRutaEstatica() throws Exception {
        // Valida que las vistas y archivos estáticos (no inician con /api/) sean públicos
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/dashboard.html");

        boolean result = interceptor.preHandle(request, response, new Object());

        assertTrue("Vistas y archivos estáticos no protegidos deberían ser públicos", result);
        verifyNoInteractions(response, usuarioService);
    }

    @Test
    public void testPreHandleFaltaCabeceraXUsuarioId() throws Exception {
        // Valida rechazo si no se envía la cabecera 'X-Usuario-Id'
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/almacen");
        when(request.getHeader("X-Usuario-Id")).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        boolean result = interceptor.preHandle(request, response, new Object());

        assertFalse("Debería retornar false por falta de cabecera", result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json;charset=UTF-8");
        assertTrue(stringWriter.toString().contains("Falta cabecera X-Usuario-Id"));
    }

    @Test
    public void testPreHandleUsuarioInexistente() throws Exception {
        // Valida rechazo si el usuario en la cabecera no está registrado
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/almacen");
        when(request.getHeader("X-Usuario-Id")).thenReturn("99");
        when(usuarioService.buscarPorId(99)).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        boolean result = interceptor.preHandle(request, response, new Object());

        assertFalse("Debería retornar false si el usuario no existe", result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Usuario no encontrado o inexistente"));
    }

    @Test
    public void testPreHandleAccesoExitoso() throws Exception {
        // Valida acceso permitido si la cabecera es de un usuario válido
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/almacen");
        when(request.getHeader("X-Usuario-Id")).thenReturn("1");
        when(usuarioService.buscarPorId(1)).thenReturn(new Usuario(1, "Admin", "General", "123"));

        boolean result = interceptor.preHandle(request, response, new Object());

        assertTrue("Debería retornar true si el ID en cabecera es de un usuario registrado", result);
    }
}
