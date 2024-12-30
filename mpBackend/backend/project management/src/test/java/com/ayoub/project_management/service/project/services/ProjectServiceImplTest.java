package com.ayoub.project_management.service.project.services;

import com.ayoub.project_management.Repository.ProjectRepository;
import com.ayoub.project_management.model.Chat;
import com.ayoub.project_management.model.Issue;
import com.ayoub.project_management.model.Project;
import com.ayoub.project_management.model.User;
import com.ayoub.project_management.service.chat.ChatService;
import com.ayoub.project_management.service.project.ProjectServiceImpl;
import com.ayoub.project_management.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProjectServiceImplTest {

    @Captor
    private ArgumentCaptor<Project> projectCaptor;

    @Captor
    private ArgumentCaptor<Chat> chatCaptor;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserService userService;

    @Mock
    private ChatService chatService;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProject() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setFullName("John Doe");

        Project inputProject = new Project();
        inputProject.setName("Project Name");
        inputProject.setDescription("Project Description");
        inputProject.setStartDate(LocalDate.now());
        inputProject.setEndDate(LocalDate.now().plusDays(10));
        inputProject.setBudget(10000.0);

        Project savedProject = new Project();
        savedProject.setId(1L);
        savedProject.setName(inputProject.getName());
        savedProject.setDescription(inputProject.getDescription());

        Chat chat = new Chat();
        chat.setId(1L);
        chat.setName("Chat for Project");

        when(projectRepository.save(projectCaptor.capture())).thenReturn(savedProject);
        when(chatService.createChat(chatCaptor.capture())).thenReturn(chat);

        // Act
        Project result = projectService.createProject(inputProject, user);

        // Assert
        verify(projectRepository).save(projectCaptor.capture());
        verify(chatService).createChat(chatCaptor.capture());

        Project capturedProject = projectCaptor.getValue();
        assertEquals(inputProject.getName(), capturedProject.getName());
        assertEquals(inputProject.getDescription(), capturedProject.getDescription());
        assertTrue(capturedProject.getTeam().contains(user));

        Chat capturedChat = chatCaptor.getValue();
        assertEquals(savedProject, capturedChat.getProject());

        assertNotNull(result);
        assertEquals(savedProject.getName(), result.getName());
        assertEquals(chat, result.getChat());
    }

    @Test
    void testGetAllProjectsByTeam() throws Exception {
        User user = new User();
        user.setId(1L);

        Project project = new Project();
        project.setTeam(Collections.singletonList(user));
        Issue a=new Issue();
        Issue b=new Issue();
        a.setStatus("Open");
        b.setStatus("Closed");
        project.setIssues(Arrays.asList( a,b ));


        when(projectRepository.findByTeamContainingOrOwner(user, user))
                .thenReturn(Collections.singletonList(project));

        List<Project> projects = projectService.getAllProjectsByTeam(user, null, null);

        assertNotNull(projects);
        assertEquals(1, projects.size());
        assertEquals(50.0f, projects.get(0).getProgress());
    }

    @Test
    void testGetProjectById() throws Exception {
        Project project = new Project();
        project.setId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project result = projectService.getProjectById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(projectRepository).findById(1L);
    }

    @Test
    void testGetProjectById_NotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            projectService.getProjectById(1L);
        });

        assertEquals("404 NOT_FOUND \"Project not found\"", exception.getMessage());
    }

    @Test
    void testDeleteProject() throws Exception {
        projectService.deleteProject(1L, 1L);
        verify(projectRepository).deleteById(1L);
    }

    @Test
    void testUpdateProject() throws Exception {
        // Arrange
        Project project = new Project();
        project.setName("Updated Name");
        project.setDescription("Updated Description");

        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("Old Name");
        existingProject.setDescription("Old Description");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(projectCaptor.capture())).thenReturn(existingProject);

        // Act
        Project result = projectService.updateProject(project, 1L);

        // Assert
        verify(projectRepository).save(projectCaptor.capture());

        // Capture the updated project and check that it's modified
        Project capturedProject = projectCaptor.getValue();
        assertNotNull(result);
        assertEquals("Updated Name", capturedProject.getName());
        assertEquals("Updated Description", capturedProject.getDescription());

        // Make sure the result has the updated values
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    void testAddUserToProject() throws Exception {
        Project project = new Project();
        project.setId(1L);
        project.setTeam(new ArrayList<>());

        Chat chat = new Chat();
        chat.setUsers(new ArrayList<>());
        project.setChat(chat);

        User user = new User();
        user.setId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userService.findUserById(1L)).thenReturn(user);

        projectService.addUserToProject(1L, 1L);

        assertTrue(project.getTeam().contains(user));
        assertTrue(chat.getUsers().contains(user));
        verify(projectRepository).save(project);
    }

    @Test
    void testRemoveUserFromProject() throws Exception {
        Project project = new Project();
        project.setId(1L);
        project.setTeam(new ArrayList<>());

        Chat chat = new Chat();
        chat.setUsers(new ArrayList<>());
        project.setChat(chat);

        User user = new User();
        user.setId(1L);
        project.getTeam().add(user);
        chat.getUsers().add(user);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userService.findUserById(1L)).thenReturn(user);

        projectService.removeUserToProject(1L, 1L);

        assertFalse(project.getTeam().contains(user));
        assertFalse(chat.getUsers().contains(user));
        verify(projectRepository).save(project);
    }

    @Test
    void testSearchProject() throws Exception {
        User user = new User();
        user.setId(1L);

        Project project = new Project();
        project.setName("Test Project");

        when(projectRepository.findByNameContainingIgnoreCaseAndTeamContains("test", user))
                .thenReturn(Collections.singletonList(project));

        List<Project> result = projectService.searchProject("test", user);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Project", result.get(0).getName());
    }

    @Test
    void testGetChatById() throws Exception {
        Project project = new Project();
        project.setId(1L);
        Chat chat = new Chat();
        project.setChat(chat);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Chat result = projectService.getChatById(1L);

        assertNotNull(result);
        assertEquals(chat, result);
    }
}
