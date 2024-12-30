package com.ayoub.project_management.service.project.repoTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ayoub.project_management.Repository.InvitationRepository;
import com.ayoub.project_management.model.Invitation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

 class InvitationRepositoryTest {

    @Mock
    private InvitationRepository invitationRepository;

    private Invitation invitation;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create sample Invitation data
        invitation = new Invitation(1L, "sampleToken123", "test@example.com", 1L);
    }

    @Test
     void testFindByToken() {
        // Given
        String token = "sampleToken123";
        when(invitationRepository.findByToken(token))
                .thenReturn(invitation);

        // When
        Invitation result = invitationRepository.findByToken(token);

        // Then
        assertNotNull(result);
        assertEquals(token, result.getToken());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
     void testFindByToken_NotFound() {
        // Given
        String token = "nonExistingToken";
        when(invitationRepository.findByToken(token))
                .thenReturn(null);

        // When
        Invitation result = invitationRepository.findByToken(token);

        // Then
        assertNull(result);
    }

    @Test
     void testFindByEmail() {
        // Given
        String email = "test@example.com";
        when(invitationRepository.findByEmail(email))
                .thenReturn(invitation);

        // When
        Invitation result = invitationRepository.findByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("sampleToken123", result.getToken());
    }

    @Test
    public void testFindByEmail_NotFound() {
        // Given
        String email = "nonExistingEmail@example.com";
        when(invitationRepository.findByEmail(email))
                .thenReturn(null);

        // When
        Invitation result = invitationRepository.findByEmail(email);

        // Then
        assertNull(result);
    }
}
