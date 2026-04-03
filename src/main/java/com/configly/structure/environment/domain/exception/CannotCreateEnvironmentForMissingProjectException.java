package com.configly.structure.environment.domain.exception;


import com.configly.model.project.ProjectId;

public class CannotCreateEnvironmentForMissingProjectException extends RuntimeException {

    public CannotCreateEnvironmentForMissingProjectException(ProjectId projectId) {
        super("Cannot create environment for missing project: " + projectId.toString());
    }
}
