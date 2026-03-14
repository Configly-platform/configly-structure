package pl.feature.toggle.service.configuration.environment.domain.exception;


import pl.feature.toggle.service.model.project.ProjectId;

public class CannotOperateOnEnvironmentForArchivedProjectException extends RuntimeException {

    public CannotOperateOnEnvironmentForArchivedProjectException(ProjectId projectId) {
        super("Cannot operate on environment for archived project: " + projectId.toString());
    }
}
