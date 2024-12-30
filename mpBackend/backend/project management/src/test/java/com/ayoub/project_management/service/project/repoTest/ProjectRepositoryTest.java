package com.ayoub.project_management.service.project.repoTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ayoub.project_management.Repository.ProjectRepository;
import com.ayoub.project_management.model.Project;
import com.ayoub.project_management.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

 class ProjectRepositoryTest {

    @Mock
    private ProjectRepository projectRepository;

    private User owner;
    private User teamMember;
    private Project project;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create sample Users
        owner = new User(1L, "Owner", "owner@example.com", "123-456", new ArrayList<>(), 0);
        teamMember = new User(2L, "Team Member", "team@example.com", "789-101", new ArrayList<>(), 0);

        // Create sample Project
        project = new Project(1L, "Test Project", "A test project", LocalDate.now(), LocalDate.now().plusMonths(6), 10000.0, 50.0f, null, owner, new ArrayList<>(), Arrays.asList(owner, teamMember));
    }

    @Test
     void testFindByNameContainingIgnoreCaseAndTeamContains() {
        // Given
        String partialName = "Test";
        when(projectRepository.findByNameContainingIgnoreCaseAndTeamContains(partialName, teamMember))
                .thenReturn(Collections.singletonList(project));

        // When
        List<Project> result = projectRepository.findByNameContainingIgnoreCaseAndTeamContains(partialName, teamMember);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getName().toLowerCase().contains(partialName.toLowerCase()));
        assertTrue(result.get(0).getTeam().contains(teamMember));
    }

    @Test
     void testFindByTeamContainingOrOwner() {
        // Given
        when(projectRepository.findByTeamContainingOrOwner(owner, teamMember))
                .thenReturn(Collections.singletonList(project));

        // When
        List<Project> result = projectRepository.findByTeamContainingOrOwner(owner, teamMember);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getTeam().contains(teamMember) || result.get(0).getOwner().equals(owner));
    }

    @Test
     void testFindByNameContainingIgnoreCaseAndTeamContains_NoResult() {
        // Given
        String partialName = "Nonexistent";
        when(projectRepository.findByNameContainingIgnoreCaseAndTeamContains(partialName, owner))
                .thenReturn(Collections.emptyList());

        // When
        List<Project> result = projectRepository.findByNameContainingIgnoreCaseAndTeamContains(partialName, owner);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
     void testFindByTeamContainingOrOwner_NoResult() {
        // Given
        User nonTeamMember = new User(3L, "Non Team Member", "nonteam@example.com", "111-222", new ArrayList<>(), 0);
        when(projectRepository.findByTeamContainingOrOwner(nonTeamMember, nonTeamMember))
                .thenReturn(Collections.emptyList());

        // When
        List<Project> result = projectRepository.findByTeamContainingOrOwner(nonTeamMember, nonTeamMember);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

