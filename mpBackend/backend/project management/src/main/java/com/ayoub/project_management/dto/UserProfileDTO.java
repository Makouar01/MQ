package com.ayoub.project_management.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserProfileDTO {
    private String fullName;
    private String email;
    private List<AssignedIssueDTO> assignedIssues;
}
