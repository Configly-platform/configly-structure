package com.configly.structure.project.domain.exception;

import com.configly.model.project.ProjectId;

public class CannotOperateOnArchivedProjectException extends RuntimeException {

    public CannotOperateOnArchivedProjectException(ProjectId projectId) {
        super(String.format("Cannot operate on archived project: %s", projectId.uuid()));
    }
}
