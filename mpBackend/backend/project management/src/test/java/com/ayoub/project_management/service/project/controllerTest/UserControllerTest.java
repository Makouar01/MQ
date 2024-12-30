package com.ayoub.project_management.service.project.controllerTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import com.ayoub.project_management.controller.UserController;
import com.ayoub.project_management.dto.AssignedIssueDTO;
import com.ayoub.project_management.dto.UserProfileDTO;
import com.ayoub.project_management.model.Issue;
import com.ayoub.project_management.model.Project;
import com.ayoub.project_management.model.User;
import com.ayoub.project_management.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController();
        userController.userService = userService;
    }

    @Test
    void testGetUserProfile() throws Exception {
        // Mock input
        String token = "Bearer sampleJwtToken";

        // Mock user
        User mockUser = new User();
        mockUser.setFullName("John Doe");
        mockUser.setEmail("john.doe@example.com");

        // Mock assigned issues
        List<Issue> mockIssues = new ArrayList<>();

        Issue issue1 = new Issue();
        issue1.setTitle("Issue 1");
        issue1.setStatus("Open");
        issue1.setDueDate(LocalDate.of(2024, 12, 31));
        Project project1 = new Project();
        project1.setName("Project A");
        issue1.setProject(project1);

        Issue issue2 = new Issue();
        issue2.setTitle("Issue 2");
        issue2.setStatus("Closed");
        issue2.setDueDate(LocalDate.of(2024, 11, 30));
        issue2.setProject(null); // No project assigned

        mockIssues.add(issue1);
        mockIssues.add(issue2);

        mockUser.setAssignedIssues(mockIssues);

        // Mock service behavior
        when(userService.findUserProfileByJwt(token)).thenReturn(mockUser);

        // Call the controller method
        ResponseEntity<UserProfileDTO> response = userController.getUserProfile(token);

        // Assertions
        assertEquals(200, response.getStatusCodeValue(), "Expected HTTP Status 200 OK");

        UserProfileDTO responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("John Doe", responseBody.getFullName(), "Expected full name to match");
        assertEquals("john.doe@example.com", responseBody.getEmail(), "Expected email to match");

        // Verify assigned issues
        List<AssignedIssueDTO> assignedIssues = responseBody.getAssignedIssues();
        assertNotNull(assignedIssues, "Assigned issues should not be null");
        assertEquals(2, assignedIssues.size(), "Expected two assigned issues");

        AssignedIssueDTO issueDTO1 = assignedIssues.get(0);
        assertEquals("Issue 1", issueDTO1.getTitle(), "Expected title of first issue to match");
        assertEquals("Open", issueDTO1.getStatus(), "Expected status of first issue to match");
        assertEquals(LocalDate.of(2024, 12, 31), issueDTO1.getDueDate(), "Expected due date of first issue to match");
        assertEquals("Project A", issueDTO1.getProjectName(), "Expected project name of first issue to match");

        AssignedIssueDTO issueDTO2 = assignedIssues.get(1);
        assertEquals("Issue 2", issueDTO2.getTitle(), "Expected title of second issue to match");
        assertEquals("Closed", issueDTO2.getStatus(), "Expected status of second issue to match");
        assertEquals(LocalDate.of(2024, 11, 30), issueDTO2.getDueDate(), "Expected due date of second issue to match");
        assertEquals("N/A", issueDTO2.getProjectName(), "Expected project name of second issue to be 'N/A'");

        // Verify service interaction
        verify(userService, times(1)).findUserProfileByJwt(token);
    }
}
