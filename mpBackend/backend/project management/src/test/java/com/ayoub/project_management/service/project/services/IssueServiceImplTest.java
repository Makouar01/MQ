package com.ayoub.project_management.service.project.services;

import com.ayoub.project_management.Repository.IssueRepository;
import com.ayoub.project_management.model.Issue;
import com.ayoub.project_management.model.Project;
import com.ayoub.project_management.model.User;
import com.ayoub.project_management.request.IssueRequest;
import com.ayoub.project_management.service.issue.IssueServiceImpl;
import com.ayoub.project_management.service.project.ProjectService;
import com.ayoub.project_management.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssueServiceImplTest {

    @InjectMocks
    private IssueServiceImpl issueService;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private ProjectService projectService;

    @Mock
    private UserService userService;

    @Test
    void testGetIssueById() throws Exception {
        Long issueId = 1L;
        Issue issue = new Issue();
        issue.setId(issueId);

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(issue));

        Issue result = issueService.getIssueById(issueId);

        assertNotNull(result);
        assertEquals(issueId, result.getId());
    }

    @Test
    public void testGetIssueById_NotFound() {
        // Arrange
        Long issueId = 1L;
        when(issueRepository.findById(issueId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> issueService.getIssueById(issueId));
    }

    @Test
    void testGetIssueByProjectId() throws Exception {
        Long projectId = 1L;
        Issue issue = new Issue();
        issue.setProjectId(projectId);

        when(issueRepository.findByProjectId(projectId)).thenReturn(Collections.singletonList(issue));

        List<Issue> result = issueService.getIssueByProjectId(projectId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(projectId, result.get(0).getProjectId());
    }

    @Test
    void testCreateIssue() throws Exception {
        Long projectId = 1L;
        User user = new User();
        user.setId(1L);
        Project project = new Project();
        project.setId(projectId);

        IssueRequest issueRequest = new IssueRequest();
        issueRequest.setProjectId(projectId);
        issueRequest.setTitle("New Issue");
        issueRequest.setStatus("Open");

        when(projectService.getProjectById(projectId)).thenReturn(project);
        when(issueRepository.save(any(Issue.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Issue result = issueService.createIssue(issueRequest, user);

        assertNotNull(result);
        assertEquals("New Issue", result.getTitle());
        assertEquals("Open", result.getStatus());
        assertEquals(projectId, result.getProjectId());
    }

    @Test
    void testDeleteIssue() throws Exception {
        Long issueId = 1L;

        Issue issue = new Issue();
        issue.setId(issueId);

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(issue));
        doNothing().when(issueRepository).deleteById(issueId);

        issueService.deleteIssue(issueId, 1L);

        verify(issueRepository, times(1)).deleteById(issueId);
    }

    @Test
    void testAddUserToIssue() throws Exception {
        Long issueId = 1L;
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Issue issue = new Issue();
        issue.setId(issueId);

        when(userService.findUserById(userId)).thenReturn(user);
        when(issueRepository.findById(issueId)).thenReturn(Optional.of(issue));
        when(issueRepository.save(any(Issue.class))).thenReturn(issue);

        Issue result = issueService.addUserToIssue(issueId, userId);

        assertNotNull(result);
        assertEquals(userId, result.getAssignee().getId());
    }

    @Test
    void testUpdateStatus() throws Exception {
        Long issueId = 1L;
        String status = "In Progress";
        Issue issue = new Issue();
        issue.setId(issueId);

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(issue));
        when(issueRepository.save(any(Issue.class))).thenReturn(issue);

        Issue result = issueService.updateStatus(issueId, status);

        assertNotNull(result);
        assertEquals(status, result.getStatus());
    }
}

