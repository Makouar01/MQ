package com.ayoub.project_management.service.project.services;

import com.ayoub.project_management.Repository.MessageRepository;
import com.ayoub.project_management.Repository.UserRepository;
import com.ayoub.project_management.model.Chat;
import com.ayoub.project_management.model.Message;
import com.ayoub.project_management.model.Project;
import com.ayoub.project_management.model.User;
import com.ayoub.project_management.service.message.MessageServiceImpl;
import com.ayoub.project_management.service.project.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @InjectMocks
    private MessageServiceImpl messageService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectService projectService;

    @Mock
    private Chat chat;

    @Mock
    private Project project;

    @Mock
    private User user;

    @Test
    void testSendMessage_Success() throws Exception {
        Long userId = 1L;
        Long projectId = 100L;
        String content = "Test message";

        // Mocking the User and Chat retrieval
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(projectService.getProjectById(projectId)).thenReturn(project);
        when(project.getChat()).thenReturn(chat);

        // Creating a new message and mocking its save behavior
        Message message = new Message(null, content, LocalDateTime.now(), chat, user);
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        // Act
        Message result = messageService.sendMessage(userId, projectId, content);

        // Assert
        assertNotNull(result);
        assertEquals(content, result.getContent());
        assertEquals(user, result.getSender());
        assertEquals(chat, result.getChat());

        // Verifying interactions
        verify(userRepository, times(1)).findById(userId);
        verify(projectService, times(1)).getProjectById(projectId);
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void testSendMessage_UserNotFound() {
        Long userId = 1L;
        Long projectId = 100L;
        String content = "Test message";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> messageService.sendMessage(userId, projectId, content));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(projectService, messageRepository, chat);
    }

    @Test
    void testGetMessageByProjectId_Success() throws Exception {
        Long projectId = 100L;
        Long chatId = 1L;

        List<Message> messages = List.of(
                new Message(null, "First message", LocalDateTime.now(), chat, user),
                new Message(null, "Second message", LocalDateTime.now(), chat, user)
        );

        when(projectService.getProjectById(projectId)).thenReturn(project);
        when(project.getChat()).thenReturn(chat);
        when(chat.getId()).thenReturn(chatId); // Mocked behavior
        when(messageRepository.findByChatIdOrderByCreatedAtAsc(chatId)).thenReturn(messages);

        List<Message> result = messageService.getMessageByProjectId(projectId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("First message", result.get(0).getContent());
        assertEquals("Second message", result.get(1).getContent());

        verify(projectService, times(1)).getProjectById(projectId);
        verify(messageRepository, times(1)).findByChatIdOrderByCreatedAtAsc(chatId);
    }

    @Test
    void testGetMessageByProjectId_ProjectNotFound() throws Exception {
        Long projectId = 100L;

        when(projectService.getProjectById(projectId)).thenThrow(new RuntimeException("Project not found"));

        Exception exception = assertThrows(RuntimeException.class,
                () -> messageService.getMessageByProjectId(projectId));

        assertEquals("Project not found", exception.getMessage());
        verify(projectService, times(1)).getProjectById(projectId);
        verifyNoInteractions(messageRepository);
    }
}


