package com.configly.structure.project.domain.exception;


import com.configly.model.project.ProjectName;

public class ProjectAlreadyExistsException extends RuntimeException {
    public ProjectAlreadyExistsException(ProjectName projectName) {
        super(String.format("Project with name '%s' already exists", projectName.value()));
    }
}
