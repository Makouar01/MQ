package com.ayoub.project_management.service.project;

import com.ayoub.project_management.config.JwtProvider;
import com.ayoub.project_management.config.JwtTokenEditor;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.crypto.SecretKey;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenEditorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtTokenEditor jwtTokenEditor;

    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenEditor = new JwtTokenEditor();
        secretKey = JwtProvider.secretKey;
    }

    @Test
    void testValidToken() throws ServletException, IOException {
        String token = Jwts.builder()
                .setSubject("test@example.com")
                .claim("email", "test@example.com")
                .claim("authorities", "ROLE_USER")
                .signWith(secretKey)
                .compact();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        jwtTokenEditor.doFilterInternal(request, response, filterChain);

        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication, "Authentication object should not be null");
        assertEquals("test@example.com", authentication.getName(), "Authenticated username should match the token");
        assertEquals("ROLE_USER", authentication.getAuthorities().iterator().next().getAuthority(), "Role should match the token claims");

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testInvalidToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");

        assertThrows(Exception.class, () -> jwtTokenEditor.doFilterInternal(request, response, filterChain),
                "Should throw an exception for an invalid token");

        verify(filterChain, times(0)).doFilter(request, response);
    }

    @Test
    void testNoToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtTokenEditor.doFilterInternal(request, response, filterChain);

        assertNull(null, "Authentication should remain null with no token");
        verify(filterChain, times(1)).doFilter(request, response);
    }

}