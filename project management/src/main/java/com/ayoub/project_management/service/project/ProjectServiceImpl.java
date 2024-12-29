package com.ayoub.project_management.service.project;

import com.ayoub.project_management.Repository.ProjectRepository;
import com.ayoub.project_management.model.Chat;
import com.ayoub.project_management.model.Issue;
import com.ayoub.project_management.model.Project;
import com.ayoub.project_management.model.User;
import com.ayoub.project_management.service.chat.ChatService;
import com.ayoub.project_management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ChatService chatService;
    @Override
    public Project createProject(Project project, User user) throws Exception {

        Project createdProject = new Project();
        createdProject.setName(project.getName());
        createdProject.setDescription(project.getDescription());
        createdProject.setStartDate(project.getStartDate());
        createdProject.setEndDate(project.getEndDate());
        createdProject.setBudget(project.getBudget());
        createdProject.getTeam().add(user);
        createdProject.setOwner(user);

        Project savedProject = projectRepository.save(createdProject);

        Chat chat = new Chat();
        chat.setProject(savedProject);

        Chat projectChat = chatService.createChat(chat);
        savedProject.setChat(projectChat);



        return savedProject;
    }

    @Override
    public List<Project> getAllProjectsByTeam(User user, String category, String tag) throws Exception {
        // Récupérer les projets où l'utilisateur est soit dans l'équipe soit le propriétaire
        List<Project> projects = projectRepository.findByTeamContainingOrOwner(user, user);

        // Mettre à jour la valeur de `progress` pour chaque projet
        for (Project project : projects) {
            List<Issue> issues = project.getIssues(); // Récupérer les issues du projet

            if (issues != null && !issues.isEmpty()) {
                // Calculer le nombre d'issues fermées
                long closedIssuesCount = issues.stream()
                        .filter(issue -> "Closed".equalsIgnoreCase(issue.getStatus()))
                        .count();

                // Mettre à jour la valeur de progress (en pourcentage)
                project.setProgress((float) closedIssuesCount / issues.size() * 100);
            } else {
                // Si aucun issue, progress est 0
                project.setProgress(0f);
            }
        }

        return projects;
    }


    @Override
    public Project getProjectById(Long projectId) throws Exception {
        Optional<Project> project = projectRepository.findById(projectId);
        project.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Project not found"));
        return project.get();
    }

    @Override
    public void deleteProject(Long projectId, Long UserId) throws Exception {
        projectRepository.deleteById(projectId);

    }

    @Override
    public Project updateProject(Project project, Long Id) throws Exception {
        Project updatedProject = getProjectById(Id);
        updatedProject.setName(project.getName());
        updatedProject.setDescription(project.getDescription());

        Project savedProject = projectRepository.save(updatedProject);

        return savedProject;
    }

    @Override
    public void addUserToProject(Long projectId, Long UserId) throws Exception {
        Project project = getProjectById(projectId);
        User user = userService.findUserById(UserId);
        if(!project.getTeam().contains(user)){
            project.getChat().getUsers().add(user);
            project.getTeam().add(user);
        }
        projectRepository.save(project);
    }

    @Override
    public void removeUserToProject(Long projectId, Long UserId) throws Exception {
        Project project = getProjectById(projectId);
        User user = userService.findUserById(UserId);
        if(project.getTeam().contains(user)){
            project.getChat().getUsers().remove(user);
            project.getTeam().remove(user);
        }
        projectRepository.save(project);
    }

    @Override
    public List<Project> searchProject(String keyword, User user) throws Exception {
        List<Project> projects = projectRepository.findByNameContainingIgnoreCaseAndTeamContains(keyword.toLowerCase(), user);
        System.out.println("Keyword: " + keyword);

        return projects;
    }

    @Override
    public Chat getChatById(Long chatId) throws Exception {
        Project project = getProjectById(chatId);

        return project.getChat();
    }
}



