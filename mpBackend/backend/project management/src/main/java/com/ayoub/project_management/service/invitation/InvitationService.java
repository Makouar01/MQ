package com.ayoub.project_management.service.invitation;

import com.ayoub.project_management.model.Invitation;
import jakarta.mail.MessagingException;

public interface InvitationService {

    public void sendInvitation(String email, Long projectId) throws MessagingException;
    public Invitation acceptInvitation(String token) throws Exception;
    public String getTokenByUserEmail(String userEmail);
    public void deleteToken(String token);
    boolean isInvitaionAlreadyAccepted(String token);

}

