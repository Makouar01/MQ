package com.ayoub.project_management.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AssignedIssueDTO {
    private String title;
    private String status;
    private LocalDate dueDate;
    private String projectName;
}
