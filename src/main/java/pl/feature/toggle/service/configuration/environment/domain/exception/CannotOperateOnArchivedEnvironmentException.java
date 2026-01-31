package pl.feature.toggle.service.configuration.environment.domain.exception;

import pl.feature.toggle.service.model.environment.EnvironmentId;

public class CannotOperateOnArchivedEnvironmentException extends RuntimeException {

    public CannotOperateOnArchivedEnvironmentException(EnvironmentId environmentId) {
        super("Cannot operate on archived environment: " + environmentId.idAsString());
    }
}
