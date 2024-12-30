package com.ayoub.project_management.service.project.services;

import com.ayoub.project_management.Repository.InvitationRepository;
import com.ayoub.project_management.model.Invitation;
import com.ayoub.project_management.service.Email.EmailService;
import com.ayoub.project_management.service.invitation.InvitationServiceImpl;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvitationServiceImplTest {

    @InjectMocks
    private InvitationServiceImpl invitationService;

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private EmailService emailService;

    @Test
    void testSendInvitation_Success() throws Exception {
        String email = "test@example.com";
        Long projectId = 1L;

        doNothing().when(emailService).sendEmailWithToken(anyString(), anyString());
        when(invitationRepository.save(any(Invitation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> invitationService.sendInvitation(email, projectId));

        verify(emailService, times(1)).sendEmailWithToken(eq(email), anyString());
        verify(invitationRepository, times(1)).save(any(Invitation.class));
    }

    @Test
    void testSendInvitation_Failure() throws Exception {
        String email = "test@example.com";
        Long projectId = 1L;

        doThrow(new MessagingException("Email sending failed")).when(emailService).sendEmailWithToken(anyString(), anyString());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> invitationService.sendInvitation(email, projectId));

        assertEquals("Failed to send invitation email. Please try again later.", exception.getMessage());
        verify(emailService, times(1)).sendEmailWithToken(eq(email), anyString());
        verify(invitationRepository, never()).save(any(Invitation.class));
    }

    @Test
    void testAcceptInvitation_Success() throws Exception {
        String token = "valid-token";
        Invitation invitation = new Invitation();
        invitation.setToken(token);

        when(invitationRepository.findByToken(token)).thenReturn(invitation);

        Invitation result = invitationService.acceptInvitation(token);

        assertNotNull(result);
        assertEquals(token, result.getToken());
        verify(invitationRepository, times(1)).findByToken(token);
    }

    @Test
    void testAcceptInvitation_InvalidToken() {
        String token = "invalid-token";

        when(invitationRepository.findByToken(token)).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> invitationService.acceptInvitation(token));

        assertEquals("Invalid Invitation token", exception.getMessage());
        verify(invitationRepository, times(1)).findByToken(token);
    }

    @Test
    void testGetTokenByUserEmail_Success() {
        String email = "test@example.com";
        String token = "valid-token";
        Invitation invitation = new Invitation();
        invitation.setEmail(email);
        invitation.setToken(token);

        when(invitationRepository.findByEmail(email)).thenReturn(invitation);

        String result = invitationService.getTokenByUserEmail(email);

        assertNotNull(result);
        assertEquals(token, result);
        verify(invitationRepository, times(1)).findByEmail(email);
    }

    @Test
    void testDeleteToken_Success() {
        String token = "valid-token";
        Invitation invitation = new Invitation();
        invitation.setToken(token);

        when(invitationRepository.findByToken(token)).thenReturn(invitation);
        doNothing().when(invitationRepository).delete(invitation);

        assertDoesNotThrow(() -> invitationService.deleteToken(token));

        verify(invitationRepository, times(1)).findByToken(token);
        verify(invitationRepository, times(1)).delete(invitation);
    }

    @Test
    void testIsInvitationAlreadyAccepted_TokenNotFound() {
        String token = "nonexistent-token";

        when(invitationRepository.findByToken(token)).thenReturn(null);

        boolean result = invitationService.isInvitaionAlreadyAccepted(token);

        assertTrue(result);
        verify(invitationRepository, times(1)).findByToken(token);
    }

    @Test
    void testIsInvitationAlreadyAccepted_TokenFound() {
        String token = "valid-token";
        Invitation invitation = new Invitation();
        invitation.setToken(token);

        when(invitationRepository.findByToken(token)).thenReturn(invitation);

        boolean result = invitationService.isInvitaionAlreadyAccepted(token);

        assertFalse(result);
        verify(invitationRepository, times(1)).findByToken(token);
    }
}

