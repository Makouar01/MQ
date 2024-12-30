package com.ayoub.project_management.service.project.services;

import com.ayoub.project_management.Repository.UserRepository;
import com.ayoub.project_management.config.JwtProvider;
import com.ayoub.project_management.model.User;
import com.ayoub.project_management.service.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Test
    void testFindUserProfileByJwt() throws Exception {
        String jwt = "mockJwt";
        String email = "test@example.com";

        User user = new User();
        user.setEmail(email);

        // Mock JWT provider static method (you'll need PowerMock or other tools for static mocking)
        try (MockedStatic<JwtProvider> jwtProviderMock = Mockito.mockStatic(JwtProvider.class)) {
            jwtProviderMock.when(() -> JwtProvider.getEmailFromToken(jwt)).thenReturn(email);

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

            User result = userService.findUserProfileByJwt(jwt);

            assertNotNull(result);
            assertEquals(email, result.getEmail());
        }
    }

    @Test
    void testFindUserByEmail() throws Exception {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User result = userService.findUserByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testFindUserByEmail_UserNotFound() {
        String email = "notfound@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> userService.findUserByEmail(email));

        assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) exception).getStatusCode());
        assertEquals("User Not Found", ((ResponseStatusException) exception).getReason());
    }

    @Test
    void testFindUserById() throws Exception {
        Long id = 1L;
        User user = new User();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.findUserById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testFindUserById_UserNotFound() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> userService.findUserById(id));

        assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) exception).getStatusCode());
        assertEquals("User Not Found", ((ResponseStatusException) exception).getReason());
    }

    @Test
    void testUpdateInformation() {
        Long userId = 1L;

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("old@example.com");
        existingUser.setFullName("Old Name");
        existingUser.setPassword("oldPassword");

        User updateRequest = new User();
        updateRequest.setEmail("new@example.com");
        updateRequest.setFullName("New Name");
        updateRequest.setPassword("newPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userService.updteInformation(updateRequest, userId);

        assertNotNull(updatedUser);
        assertEquals("new@example.com", updatedUser.getEmail());
        assertEquals("New Name", updatedUser.getFullName());
        assertEquals("encodedPassword", updatedUser.getPassword());
    }

    @Test
    void testUpdateInformation_UserNotFound() {
        Long userId = 1L;

        User updateRequest = new User();
        updateRequest.setEmail("new@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> userService.updteInformation(updateRequest, userId));

        assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) exception).getStatusCode());
        assertEquals("User Not Found", ((ResponseStatusException) exception).getReason());
    }
}
