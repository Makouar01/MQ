package com.ayoub.project_management.service.project;

import com.ayoub.project_management.config.JwtProvider;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtProviderTest {

    @Test
    void testCreateToken() {
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");

        String token = JwtProvider.createToken(authentication);
        assertNotNull(token, "Token should not be null");
        assertTrue(token.startsWith("eyJ"), "Token should have a valid JWT structure");
    }

    @Test
    void testGetEmailFromToken() {
        String testEmail = "test@example.com";
        SecretKey secretKey = JwtProvider.secretKey;

        String token = Jwts.builder()
                .setIssuedAt(new java.util.Date())
                .setExpiration(new java.util.Date(new java.util.Date().getTime() + 84600000))
                .claim("email", testEmail)
                .signWith(secretKey)
                .compact();

        String extractedEmail = JwtProvider.getEmailFromToken("Bearer " + token);
        assertEquals(testEmail, extractedEmail, "Extracted email should match the original email");
    }
}