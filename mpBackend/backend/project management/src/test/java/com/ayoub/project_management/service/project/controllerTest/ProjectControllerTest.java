package com.ayoub.project_management.service.project.controllerTest;

import com.ayoub.project_management.Repository.InvitationRepository;
import com.ayoub.project_management.controller.ProjectController;
import com.ayoub.project_management.model.Chat;
import com.ayoub.project_management.model.Invitation;
import com.ayoub.project_management.model.Project;
import com.ayoub.project_management.model.User;
import com.ayoub.project_management.service.invitation.InvitationService;
import com.ayoub.project_management.service.project.ProjectService;
import com.ayoub.project_management.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureMockMvc
 class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @Mock
    private UserService userService;

    @Mock
    private InvitationService invitationService;

    @Mock
    private InvitationRepository invitationRepository;

    @InjectMocks
    private ProjectController projectController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(projectController).build();
    }


    @Test
    void testGetProjectById() throws Exception {
        // Arrange
        User user = new User();
        Project project = new Project();
        project.setId(1L);

        when(userService.findUserProfileByJwt(anyString())).thenReturn(user);
        when(projectService.getProjectById(1L)).thenReturn(project);

        // Act & Assert
        mockMvc.perform(get("/api/projects/1")
                        .header("authorization", "mock-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testCreateProject() throws Exception {
        // Arrange
        User user = new User();
        Project project = new Project();
        project.setId(1L);

        when(userService.findUserProfileByJwt(anyString())).thenReturn(user);
        when(projectService.createProject(any(Project.class), eq(user))).thenReturn(project);

        // Act & Assert
        mockMvc.perform(post("/api/projects")
                        .header("authorization", "mock-token")
                        .contentType("application/json")
                        .content("{ \"name\": \"New Project\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testUpdateProject() throws Exception {
        // Arrange
        Project project = new Project();
        project.setId(1L);

        when(projectService.updateProject(any(Project.class), eq(1L))).thenReturn(project);

        // Act & Assert
        mockMvc.perform(patch("/api/projects/1")
                        .contentType("application/json")
                        .content("{ \"name\": \"Updated Project\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testDeleteProject() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);

        when(userService.findUserProfileByJwt(anyString())).thenReturn(user);
        doNothing().when(projectService).deleteProject(eq(1L), eq(1L));

        // Act & Assert
        mockMvc.perform(delete("/api/projects/1")
                        .header("authorization", "mock-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("project Deleted Successfully"));
    }

    @Test
    void testSearchProjects() throws Exception {
        // Arrange
        User user = new User();
        List<Project> projects = List.of(new Project());

        when(userService.findUserProfileByJwt(anyString())).thenReturn(user);
        when(projectService.searchProject(anyString(), eq(user))).thenReturn(projects);

        // Act & Assert
        mockMvc.perform(get("/api/projects/search")
                        .param("keyword", "test")
                        .header("authorization", "mock-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void testGetChatByProjectId() throws Exception {
        // Arrange
        User user = new User();
        Chat chat = new Chat();

        when(userService.findUserProfileByJwt(anyString())).thenReturn(user);
        when(projectService.getChatById(1L)).thenReturn(chat);

        // Act & Assert
        mockMvc.perform(get("/api/projects/1/chat")
                        .header("authorization", "mock-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void testInviteProject() throws Exception {
        // Arrange
        doNothing().when(invitationService).sendInvitation(anyString(), anyLong());

        // Act & Assert
        mockMvc.perform(post("/api/projects/invite")
                        .header("authorization", "mock-token")
                        .contentType("application/json")
                        .content("{ \"email\": \"test@example.com\", \"projectId\": 1 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Invitation sent successfully"));
    }

    @Test
    void testAcceptInvitation() throws Exception {
        // Arrange
        User user = new User();
        Invitation invitation = new Invitation();
        invitation.setProjectId(1L);

        when(userService.findUserProfileByJwt(anyString())).thenReturn(user);
        when(invitationService.acceptInvitation(anyString())).thenReturn(invitation);
        doNothing().when(projectService).addUserToProject(anyLong(), eq(user.getId()));

        // Act & Assert
        mockMvc.perform(get("/api/projects/acceptInvitation")
                        .param("token", "mock-token")
                        .header("authorization", "mock-jwt"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.projectId").value(1L));
    }

    @Test
    void testCheckInvitationStatus() throws Exception {
        // Arrange
        String token = "mock-token";
        when(invitationService.isInvitaionAlreadyAccepted(token)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/projects/isAccepted")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Invitation has already been accepted."));
    }
}
