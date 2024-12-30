package com.ayoub.project_management.service.project.repoTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ayoub.project_management.Repository.IssueRepository;
import com.ayoub.project_management.model.Issue;
import com.ayoub.project_management.model.Project;
import com.ayoub.project_management.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

 class IssueRepositoryTest {

    @Mock
    private IssueRepository issueRepository;

    private Project project;
    private User assignee;
    private Issue issue1;
    private Issue issue2;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create sample User
        assignee = new User(1L, "Assignee", "assignee@example.com", "123-456", new ArrayList<>(), 0);

        // Create sample Project
        project = new Project(1L, "Test Project", "A test project", LocalDate.now(), LocalDate.now().plusMonths(6), 10000.0, 50.0f, null, null, new ArrayList<>(), new ArrayList<>());

        // Create sample Issues
        issue1 = new Issue(1L, "Issue 1", "Description of Issue 1", "OPEN", false, 1L, "High", LocalDate.now().plusDays(5), new ArrayList<>(), assignee, project, new ArrayList<>());
        issue2 = new Issue(2L, "Issue 2", "Description of Issue 2", "IN_PROGRESS", false, 1L, "Low", LocalDate.now().plusDays(10), new ArrayList<>(), assignee, project, new ArrayList<>());
    }

    @Test
     void testFindByProjectId() {
        // Given
        Long projectId = 1L;
        when(issueRepository.findByProjectId(projectId))
                .thenReturn(Arrays.asList(issue1, issue2));

        // When
        List<Issue> result = issueRepository.findByProjectId(projectId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(issue1));
        assertTrue(result.contains(issue2));
    }

    @Test
     void testFindByProjectId_NoIssues() {
        // Given
        Long projectId = 1L;
        when(issueRepository.findByProjectId(projectId))
                .thenReturn(Collections.emptyList());

        // When
        List<Issue> result = issueRepository.findByProjectId(projectId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
     void testFindByProjectId_IssueWithDifferentProject() {
        // Given
        Long projectId = 1L;
        Project anotherProject = new Project(2L, "Another Project", "Another test project", LocalDate.now(), LocalDate.now().plusMonths(6), 5000.0, 20.0f, null, null, new ArrayList<>(), new ArrayList<>());
        Issue issueFromAnotherProject = new Issue(3L, "Issue 3", "Description of Issue 3", "OPEN", false, 2L, "Medium", LocalDate.now().plusDays(3), new ArrayList<>(), assignee, anotherProject, new ArrayList<>());

        when(issueRepository.findByProjectId(projectId))
                .thenReturn(Arrays.asList(issue1, issue2));

        // When
        List<Issue> result = issueRepository.findByProjectId(projectId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(issue1));
        assertTrue(result.contains(issue2));
        assertFalse(result.contains(issueFromAnotherProject)); // Ensure it's not returned
    }
}

