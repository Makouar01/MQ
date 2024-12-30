package com.ayoub.project_management.service.project.controllerTest;

import com.ayoub.project_management.Repository.UserRepository;
import com.ayoub.project_management.controller.AuthController;
import com.ayoub.project_management.model.User;
import com.ayoub.project_management.request.LoginRequest;
import com.ayoub.project_management.responce.AuthResponce;
import com.ayoub.project_management.service.CustomUserServiceImpl;
import com.ayoub.project_management.service.subscription.SubscriptionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomUserServiceImpl userService;

    @Mock
    private SubscriptionServiceImpl subscriptionService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController();
        authController.userRepository = userRepository;
        authController.passwordEncoder = passwordEncoder;
        authController.userService = userService;
        authController.subscriptionService = subscriptionService;
    }

    @Test
    void testSignupEmailAlreadyExists() {
        User existingUser = new User();
        existingUser.setEmail("existing@example.com");

        when(userRepository.findByEmail("existing@example.com")).thenReturn(java.util.Optional.of(existingUser));

        User newUser = new User();
        newUser.setEmail("existing@example.com");
        newUser.setPassword("password123");

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authController.createUserHandler(newUser)
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertFalse(exception.getReason().contains("Email already exists with another account"));
    }

    @Test
    void testSignupSuccess() {
        User newUser = new User();
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("password123");
        newUser.setFullName("New User");

        when(userRepository.findByEmail("newuser@example.com")).thenReturn(java.util.Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<AuthResponce> response = authController.createUserHandler(newUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Signup success", response.getBody().getMessage());
        verify(subscriptionService, times(1)).createSubscription(any(User.class));
    }

    @Test
    void testSigninUserNotFound() {
        when(userService.loadUserByUsername("nonexistent@example.com")).thenReturn(null);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("password123");

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authController.signing(loginRequest)
        );

        assertEquals("Username not found", exception.getMessage());
    }

    @Test
    void testSigninWrongPassword() {
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getPassword()).thenReturn("encodedPassword");
        when(userService.loadUserByUsername("user@example.com")).thenReturn(mockUserDetails);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("wrongPassword");

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authController.signing(loginRequest)
        );

        assertEquals("Wrong password", exception.getMessage());
    }

    @Test
    void testSigninSuccess() {
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getPassword()).thenReturn("encodedPassword");
        when(userService.loadUserByUsername("user@example.com")).thenReturn(mockUserDetails);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("password123");

        ResponseEntity<AuthResponce> response = authController.signing(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(" signin success", response.getBody().getMessage());
    }
}