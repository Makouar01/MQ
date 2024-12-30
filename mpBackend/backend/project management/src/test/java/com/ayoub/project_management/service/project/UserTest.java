package com.ayoub.project_management.service.project;

import com.ayoub.project_management.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserProperties() {
        // Create a User
        User user = new User();
        user.setId(1L);
        user.setFullName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("secret");
        user.setProjectSize(5);

        // Verify properties
        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getFullName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("secret", user.getPassword());
        assertEquals(5, user.getProjectSize());
    }

    @Test
    void testAssignedIssuesInitialization() {
        User user = new User();
        assertNotNull(user.getAssignedIssues());
        assertTrue(user.getAssignedIssues().isEmpty());
    }
}