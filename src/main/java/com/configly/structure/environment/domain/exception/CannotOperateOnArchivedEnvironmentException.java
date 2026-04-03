package com.configly.structure.environment.domain.exception;

import com.configly.model.environment.EnvironmentId;

public class CannotOperateOnArchivedEnvironmentException extends RuntimeException {

    public CannotOperateOnArchivedEnvironmentException(EnvironmentId environmentId) {
        super("Cannot operate on archived environment: " + environmentId.idAsString());
    }
}
