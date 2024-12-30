package com.ayoub.project_management.service.project.repoTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ayoub.project_management.Repository.MessageRepository;
import com.ayoub.project_management.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

 class MessageRepositoryTest {

    @Mock
    private MessageRepository messageRepository;

    private Message message1;
    private Message message2;
    private Message message3;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create sample Message data
        message1 = new Message(1L, "Hello", LocalDateTime.of(2024, 12, 27, 10, 0), null, null);
        message2 = new Message(2L, "Hi there", LocalDateTime.of(2024, 12, 27, 10, 5), null, null);
        message3 = new Message(3L, "How are you?", LocalDateTime.of(2024, 12, 27, 10, 2), null, null);
    }

    @Test
     void testFindByChatIdOrderByCreatedAtAsc() {
        // Given
        Long chatId = 1L;
        List<Message> messages = Arrays.asList(message1, message2, message3);
        when(messageRepository.findByChatIdOrderByCreatedAtAsc(chatId))
                .thenReturn(messages);

        // When
        List<Message> result = messageRepository.findByChatIdOrderByCreatedAtAsc(chatId);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());

        // Verify the messages are ordered by createdAt in ascending order
        assertTrue(result.get(0).getCreatedAt().isBefore(result.get(1).getCreatedAt()) || result.get(0).getCreatedAt().isEqual(result.get(1).getCreatedAt()));
    }



    @Test
     void testFindByChatIdOrderByCreatedAtAsc_EmptyList() {
        // Given
        Long chatId = 2L;
        when(messageRepository.findByChatIdOrderByCreatedAtAsc(chatId))
                .thenReturn(Arrays.asList());

        // When
        List<Message> result = messageRepository.findByChatIdOrderByCreatedAtAsc(chatId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
     void testFindByChatIdOrderByCreatedAtAsc_SingleMessage() {
        // Given
        Long chatId = 1L;
        when(messageRepository.findByChatIdOrderByCreatedAtAsc(chatId))
                .thenReturn(Arrays.asList(message1));

        // When
        List<Message> result = messageRepository.findByChatIdOrderByCreatedAtAsc(chatId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(message1.getCreatedAt(), result.get(0).getCreatedAt());
    }
}
