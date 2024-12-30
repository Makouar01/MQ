package com.ayoub.project_management.service.project.services;

import com.ayoub.project_management.service.Email.EmailServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private MimeMessageHelper mimeMessageHelper;

    @Test
    void testSendEmailWithToken_Success() throws MessagingException {
        String userEmail = "test@example.com";
        String link = "http://localhost:4200/accept-invitation?token=12345";

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(mimeMessage);

        assertDoesNotThrow(() -> emailService.sendEmailWithToken(userEmail, link));

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendEmailWithToken_FailureOnSend() throws MessagingException {
        String userEmail = "test@example.com";
        String link = "http://localhost:4200/accept-invitation?token=12345";

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MailException("Email sending failed") {}).when(mailSender).send(mimeMessage);

        MessagingException exception = assertThrows(MessagingException.class,
                () -> emailService.sendEmailWithToken(userEmail, link));

        assertEquals("Failed to send email", exception.getMessage());
        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }
}

