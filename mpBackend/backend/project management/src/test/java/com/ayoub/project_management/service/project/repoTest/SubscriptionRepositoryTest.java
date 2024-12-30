package com.ayoub.project_management.service.project.repoTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ayoub.project_management.Repository.SubscriptionRepository;
import com.ayoub.project_management.model.PlanType;
import com.ayoub.project_management.model.Subscription;
import com.ayoub.project_management.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;

 class SubscriptionRepositoryTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    private Subscription subscription;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks

        // Create a user for the subscription
        user = new User();
        user.setId(1L);

        // Create a dummy Subscription object
        subscription = new Subscription(
                1L,
                LocalDate.now().minusDays(5),
                LocalDate.now().plusMonths(1),
                PlanType.FREE,
                true,
                user
        );
    }

    @Test
     void testFindByUserId() {
        // Arrange
        Long userId = 1L;
        when(subscriptionRepository.findByUserId(userId)).thenReturn(subscription);

        // Act
        Subscription foundSubscription = subscriptionRepository.findByUserId(userId);

        // Assert
        assertNotNull(foundSubscription);
        assertEquals(userId, foundSubscription.getUser().getId()); // Ensure the user ID matches
        assertEquals(PlanType.FREE, foundSubscription.getPlanType()); // Check if the plan type matches
        assertTrue(foundSubscription.isValid()); // Ensure the subscription is valid
    }

    @Test
     void testFindByUserId_NotFound() {
        // Arrange
        Long userId = 2L; // Using a different ID that doesn't exist
        when(subscriptionRepository.findByUserId(userId)).thenReturn(null); // Simulate not found

        // Act
        Subscription foundSubscription = subscriptionRepository.findByUserId(userId);

        // Assert
        assertNull(foundSubscription); // Assert that no subscription is found
    }
}

