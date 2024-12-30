package com.ayoub.project_management.service.project.controllerTest;

import com.ayoub.project_management.controller.SubscriptionController;
import com.ayoub.project_management.model.PlanType;
import com.ayoub.project_management.model.Subscription;
import com.ayoub.project_management.model.User;
import com.ayoub.project_management.service.subscription.SubscriptionService;
import com.ayoub.project_management.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class SubscriptionControllerTest {

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private UserService userService;

    private SubscriptionController subscriptionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subscriptionController = new SubscriptionController();
        subscriptionController.subscriptionService = subscriptionService;
        subscriptionController.userService = userService;
    }

    @Test
    void testGetUserSubscription() throws Exception {
        // Mock input
        String jwt = "Bearer sampleJwtToken";

        // Mock user
        User mockUser = new User();
        mockUser.setId(1L);

        // Mock subscription
        Subscription mockSubscription = new Subscription();
        mockSubscription.setId(1L);
        mockSubscription.setPlanType(PlanType.FREE);

        // Mock service behavior
        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);
        when(subscriptionService.getUsersSubscription(mockUser.getId())).thenReturn(mockSubscription);

        // Call the controller method
        ResponseEntity<Subscription> response = subscriptionController.getUserSubscription(jwt);

        // Assertions
        assertEquals(200, response.getStatusCodeValue(), "Expected HTTP Status 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(mockSubscription, response.getBody(), "Expected subscription to match the mock subscription");

        // Verify service interaction
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(subscriptionService, times(1)).getUsersSubscription(mockUser.getId());
    }

    @Test
    void testUpgradeSubscription() throws Exception {
        // Mock input
        String jwt = "Bearer sampleJwtToken";
        PlanType newPlan = PlanType.ANNUALLY;

        // Mock user
        User mockUser = new User();
        mockUser.setId(1L);

        // Mock subscription
        Subscription upgradedSubscription = new Subscription();
        upgradedSubscription.setId(1L);
        upgradedSubscription.setPlanType(newPlan);

        // Mock service behavior
        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);
        when(subscriptionService.upgradSubscription(mockUser.getId(), newPlan)).thenReturn(upgradedSubscription);

        // Call the controller method
        ResponseEntity<Subscription> response = subscriptionController.upgradeSubscription(jwt, newPlan);

        // Assertions
        assertEquals(200, response.getStatusCodeValue(), "Expected HTTP Status 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(upgradedSubscription, response.getBody(), "Expected subscription to match the upgraded subscription");

        // Verify service interaction
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(subscriptionService, times(1)).upgradSubscription(mockUser.getId(), newPlan);
    }
}