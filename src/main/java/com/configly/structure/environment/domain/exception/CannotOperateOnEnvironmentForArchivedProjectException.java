package com.configly.structure.environment.domain.exception;


import com.configly.model.project.ProjectId;

public class CannotOperateOnEnvironmentForArchivedProjectException extends RuntimeException {

    public CannotOperateOnEnvironmentForArchivedProjectException(ProjectId projectId) {
        super("Cannot operate on environment for archived project: " + projectId.toString());
    }
}
