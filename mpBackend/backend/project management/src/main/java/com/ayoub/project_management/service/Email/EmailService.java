package com.ayoub.project_management.service.Email;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmailWithToken(String userEmail,String link) throws MessagingException;}
