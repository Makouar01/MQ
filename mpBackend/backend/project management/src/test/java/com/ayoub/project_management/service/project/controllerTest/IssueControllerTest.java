package com.ayoub.project_management.service.project.controllerTest;

import com.ayoub.project_management.controller.IssueController;
import com.ayoub.project_management.model.Issue;
import com.ayoub.project_management.model.IssueDTO;
import com.ayoub.project_management.model.User;
import com.ayoub.project_management.request.IssueRequest;
import com.ayoub.project_management.responce.AuthResponce;
import com.ayoub.project_management.service.issue.IssueService;
import com.ayoub.project_management.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class IssueControllerTest {

    @Mock
    private IssueService issueService;

    @Mock
    private UserService userService;

    private IssueController issueController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        issueController = new IssueController();
        issueController.issueService = issueService;
        issueController.userService = userService;
    }

    @Test
    void testGetIssueById() throws Exception {
        Long issueId = 1L;
        Issue mockIssue = new Issue();
        mockIssue.setId(issueId);

        when(issueService.getIssueById(issueId)).thenReturn(mockIssue);

        ResponseEntity<Issue> response = issueController.getIssueById(issueId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockIssue, response.getBody());

        verify(issueService, times(1)).getIssueById(issueId);
    }

    @Test
    void testGetIssuesByProjectId() throws Exception {
        Long projectId = 1L;
        Issue mockIssue1 = new Issue();
        Issue mockIssue2 = new Issue();

        when(issueService.getIssueByProjectId(projectId)).thenReturn(List.of(mockIssue1, mockIssue2));

        ResponseEntity<List<Issue>> response = issueController.getIssuesByProjectId(projectId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(issueService, times(1)).getIssueByProjectId(projectId);
    }

    @Test
    void testCreateIssue() throws Exception {
        String token = "Bearer sampleJwt";
        IssueRequest issueRequest = new IssueRequest();
        issueRequest.setTitle("Test Issue");

        User tokenUser = new User();
        tokenUser.setId(1L);

        Issue mockIssue = new Issue();
        mockIssue.setTitle("Test Issue");

        when(userService.findUserProfileByJwt(token)).thenReturn(tokenUser);
        when(issueService.createIssue(issueRequest, tokenUser)).thenReturn(mockIssue);

        ResponseEntity<IssueDTO> response = issueController.createIssue(issueRequest, token);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(mockIssue.getTitle(), response.getBody().getTitle());

        verify(userService, times(1)).findUserProfileByJwt(token);
        verify(issueService, times(1)).createIssue(issueRequest, tokenUser);
    }

    @Test
    void testDeleteIssue() throws Exception {
        String token = "Bearer sampleJwt";
        Long issueId = 1L;

        User tokenUser = new User();
        tokenUser.setId(1L);

        when(userService.findUserProfileByJwt(token)).thenReturn(tokenUser);

        ResponseEntity<AuthResponce> response = issueController.deleteIssue(issueId, token);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Successfully deleted", response.getBody().getMessage());

        verify(userService, times(1)).findUserProfileByJwt(token);
        verify(issueService, times(1)).deleteIssue(issueId, tokenUser.getId());
    }

    @Test
    void testAssigneIssue() throws Exception {
        String token = "Bearer sampleJwt";
        Long issueId = 1L;

        User tokenUser = new User();
        tokenUser.setId(1L);

        Issue mockIssue = new Issue();
        mockIssue.setId(issueId);

        when(userService.findUserProfileByJwt(token)).thenReturn(tokenUser);
        when(issueService.addUserToIssue(issueId, tokenUser.getId())).thenReturn(mockIssue);

        ResponseEntity<Issue> response = issueController.assigneIssue(issueId, token);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(mockIssue, response.getBody());

        verify(userService, times(1)).findUserProfileByJwt(token);
        verify(issueService, times(1)).addUserToIssue(issueId, tokenUser.getId());
    }

    @Test
    void testUpdateIssueStatus() throws Exception {
        Long issueId = 1L;
        String status = "Closed";

        Issue mockIssue = new Issue();
        mockIssue.setId(issueId);
        mockIssue.setStatus(status);

        when(issueService.updateStatus(issueId, status)).thenReturn(mockIssue);

        ResponseEntity<Issue> response = issueController.updateIssueStatus(issueId, status);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(status, response.getBody().getStatus());

        verify(issueService, times(1)).updateStatus(issueId, status);
    }

    @Test
    void testGetUserId() throws Exception {
        String token = "Bearer sampleJwt";
        User tokenUser = new User();
        tokenUser.setId(1L);

        when(userService.findUserProfileByJwt(token)).thenReturn(tokenUser);

        Long userId = issueController.getUserId(token);

        assertNotNull(userId);
        assertEquals(1L, userId);

        verify(userService, times(1)).findUserProfileByJwt(token);
    }
}