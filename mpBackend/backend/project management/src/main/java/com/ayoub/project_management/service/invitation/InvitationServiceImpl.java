package com.ayoub.project_management.service.invitation;

import com.ayoub.project_management.Repository.InvitationRepository;
import com.ayoub.project_management.model.Invitation;
import com.ayoub.project_management.service.Email.EmailService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InvitationServiceImpl implements InvitationService {
    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private EmailService emailService;


    @Override
    public void sendInvitation(String email, Long projectId) {
        String invitationToken = UUID.randomUUID().toString();
        Invitation invitation = new Invitation();
        invitation.setEmail(email);
        invitation.setProjectId(projectId);
        invitation.setToken(invitationToken);

        String invitationLink = " http://localhost:4200/accept-invitation?token=" + invitationToken;

        try {
            emailService.sendEmailWithToken(email, invitationLink);
            invitationRepository.save(invitation);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send invitation email. Please try again later."); // Rethrow a custom exception
        }
    }


    @Override
    public Invitation acceptInvitation(String Token) throws Exception {
        Invitation invitation = invitationRepository.findByToken(Token);
        if(invitation==null){
            throw new Exception("Invalid Invitation token");
        }
        return invitation;
    }

    @Override
    public String getTokenByUserEmail(String UserEmail) {
        Invitation invitation = invitationRepository.findByEmail(UserEmail);
        return invitation.getToken();
    }

    @Override
    public void deleteToken(String Token) {
        Invitation invitation = invitationRepository.findByToken(Token);
            invitationRepository.delete(invitation);
    }

    @Override
    public boolean isInvitaionAlreadyAccepted(String Token) {

        if(invitationRepository.findByToken(Token)==null){
            return true;
        }
        return false;
    }
}
