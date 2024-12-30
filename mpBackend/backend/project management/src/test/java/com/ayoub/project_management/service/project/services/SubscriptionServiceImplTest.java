package com.ayoub.project_management.service.project.services;

import com.ayoub.project_management.Repository.SubscriptionRepository;
import com.ayoub.project_management.model.PlanType;
import com.ayoub.project_management.model.Subscription;
import com.ayoub.project_management.model.User;
import com.ayoub.project_management.service.subscription.SubscriptionServiceImpl;
import com.ayoub.project_management.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceImplTest {

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserService userService;

    @Test
    void testCreateSubscription_Success() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setSubscriptionStartDate(LocalDate.now());
        subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));
        subscription.setValid(true);
        subscription.setPlanType(PlanType.FREE);

        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        // Act
        Subscription result = subscriptionService.createSubscription(user);

        // Assert
        assertNotNull(result);
        assertEquals(PlanType.FREE, result.getPlanType());
        assertEquals(LocalDate.now().plusMonths(12), result.getSubscriptionEndDate());
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    @Test
    void testGetUsersSubscription_Success() throws Exception {
        // Arrange
        Long userId = 1L;
        Subscription subscription = new Subscription();
        subscription.setUser(new User());
        subscription.setPlanType(PlanType.FREE); // Initialize PlanType
        subscription.setSubscriptionEndDate(LocalDate.now().minusDays(1)); // Expired

        when(subscriptionRepository.findByUserId(userId)).thenReturn(subscription);
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        // Act
        Subscription result = subscriptionService.getUsersSubscription(userId);

        // Assert
        assertNotNull(result);
        assertEquals(PlanType.FREE, result.getPlanType());
        assertFalse(result.getSubscriptionEndDate().isAfter(LocalDate.now()));
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }


    @Test
    void testUpgradSubscription_Success_Annually() throws Exception {
        // Arrange
        Long userId = 1L;
        PlanType planType = PlanType.ANNUALLY;

        Subscription subscription = new Subscription();
        subscription.setUser(new User());
        subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));

        when(subscriptionRepository.findByUserId(userId)).thenReturn(subscription);
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        // Act
        Subscription result = subscriptionService.upgradSubscription(userId, planType);

        // Assert
        assertNotNull(result);
        assertEquals(PlanType.ANNUALLY, result.getPlanType());
        assertEquals(LocalDate.now().plusMonths(12), result.getSubscriptionEndDate());
        //verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    @Test
    void testIsValid_FreePlan() {
        // Arrange
        Subscription subscription = new Subscription();
        subscription.setPlanType(PlanType.FREE);

        // Act
        boolean isValid = subscriptionService.isValid(subscription);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testIsValid_PaidPlanExpired() {
        // Arrange
        Subscription subscription = new Subscription();
        subscription.setPlanType(PlanType.MONTHLY);
        subscription.setSubscriptionEndDate(LocalDate.now().minusDays(1)); // Expired

        // Act
        boolean isValid = subscriptionService.isValid(subscription);

        // Assert
        assertFalse(isValid);
    }
}

