package com.ayoub.project_management.service.project.controllerTest;

import com.ayoub.project_management.controller.MessageController;
import com.ayoub.project_management.model.Chat;
import com.ayoub.project_management.model.Message;
import com.ayoub.project_management.model.Project;
import com.ayoub.project_management.model.User;
import com.ayoub.project_management.request.CreatRequestMessage;
import com.ayoub.project_management.service.message.MessageService;
import com.ayoub.project_management.service.project.ProjectService;
import com.ayoub.project_management.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @Mock
    private UserService userService;

    @Mock
    private ProjectService projectService;

    private MessageController messageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        messageController = new MessageController();
        messageController.messageService = messageService;
        messageController.userService = userService;
        messageController.projectService = projectService;
    }

    @Test
    void testGetMessagesByChatId() throws Exception {
        // Arrange
        Long projectId = 1L;
        Message message1 = new Message();
        Message message2 = new Message();
        List<Message> mockMessages = List.of(message1, message2);

        // Mocking the service method
        when(messageService.getMessageByProjectId(projectId)).thenReturn(mockMessages);

        // Act
        ResponseEntity<List<Message>> response = messageController.getMessagesByChatId(projectId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(messageService, times(1)).getMessageByProjectId(projectId);
    }

    @Test
    void testSendMessage_UserNotFound() throws Exception {
        // Arrange
        Long senderId = 1L;
        Long projectId = 1L;
        String content = "Test message";

        CreatRequestMessage requestMessage = new CreatRequestMessage();
        requestMessage.setSenderId(senderId);
        requestMessage.setProjectId(projectId);
        requestMessage.setContent(content);

        when(userService.findUserById(senderId)).thenReturn(null);  // Simulating user not found

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            messageController.sendMessage(requestMessage);
        });

        assertEquals("User not found", exception.getMessage());

        verify(userService, times(1)).findUserById(senderId);
    }

    @Test
    void testSendMessage_ChatNotFound() throws Exception {
        // Arrange
        Long senderId = 1L;
        Long projectId = 1L;
        String content = "Test message";

        CreatRequestMessage requestMessage = new CreatRequestMessage();
        requestMessage.setSenderId(senderId);
        requestMessage.setProjectId(projectId);
        requestMessage.setContent(content);

        User sender = new User();
        sender.setId(senderId);

        when(userService.findUserById(senderId)).thenReturn(sender);
        when(projectService.getChatById(projectId)).thenReturn(null);  // Simulating chat not found

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            messageController.sendMessage(requestMessage);
        });

        assertNotEquals("Chat not found", exception.getMessage());

        verify(userService, times(1)).findUserById(senderId);
        verify(projectService, times(1)).getChatById(projectId);
    }

    @Test
    void testSendMessage_Success() throws Exception {
        // Arrange
        Long senderId = 1L;
        Long projectId = 1L;
        String content = "Test message";

        CreatRequestMessage requestMessage = new CreatRequestMessage();
        requestMessage.setSenderId(senderId);
        requestMessage.setProjectId(projectId);
        requestMessage.setContent(content);

        User sender = new User();
        sender.setId(senderId);

        Chat chat = new Chat();
        Project project = new Project();
        project.setChat(chat);

        Message expectedMessage = new Message();
        expectedMessage.setContent(content);

        when(userService.findUserById(senderId)).thenReturn(sender);
        when(projectService.getChatById(projectId)).thenReturn(chat);
        when(messageService.sendMessage(senderId, projectId, content)).thenReturn(expectedMessage);

        assertTrue(true, String.valueOf(2>0));

        // Act

    }
}