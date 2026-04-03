package com.configly.structure.project.domain.exception;


import com.configly.model.project.ProjectId;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(ProjectId projectId) {
        super(String.format("Project with id '%s' not found", projectId.toString()));
    }
}
