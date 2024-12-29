package com.ayoub.project_management.controller;

import com.ayoub.project_management.dto.AssignedIssueDTO;
import com.ayoub.project_management.dto.UserProfileDTO;
import com.ayoub.project_management.model.User;
import com.ayoub.project_management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile(@RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserProfileByJwt(token);

        UserProfileDTO userProfile = new UserProfileDTO();
        userProfile.setFullName(user.getFullName());
        userProfile.setEmail(user.getEmail());

        // Mapper les tâches assignées à une structure simplifiée (DTO)
        userProfile.setAssignedIssues(
                user.getAssignedIssues().stream().map(issue -> {
                    AssignedIssueDTO issueDTO = new AssignedIssueDTO();
                    issueDTO.setTitle(issue.getTitle());
                    issueDTO.setStatus(issue.getStatus());
                    issueDTO.setDueDate(issue.getDueDate());
                    issueDTO.setProjectName(issue.getProject() != null ? issue.getProject().getName() : "N/A");
                    return issueDTO;
                }).collect(Collectors.toList())
        );

        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserInformation(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.UpdteInformation(user, id);
        return ResponseEntity.ok(updatedUser);
    }
}
