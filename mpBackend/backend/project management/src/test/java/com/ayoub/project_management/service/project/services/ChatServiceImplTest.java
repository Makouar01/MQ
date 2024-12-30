package com.ayoub.project_management.service.project.services;

import com.ayoub.project_management.Repository.ChatRepository;
import com.ayoub.project_management.model.Chat;
import com.ayoub.project_management.service.chat.ChatServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

    @InjectMocks
    private ChatServiceImpl chatService;

    @Mock
    private ChatRepository chatRepository;

    @Test
    void testCreateChat_Success() {
        // Arrange
        Chat chat = new Chat();
        chat.setId(1L);
        chat.setName("Test Chat");

        when(chatRepository.save(chat)).thenReturn(chat);

        // Act
        Chat result = chatService.createChat(chat);

        // Assert
        assertNotNull(result);
        assertEquals("Test Chat", result.getName());
        verify(chatRepository, times(1)).save(chat);
    }
}

