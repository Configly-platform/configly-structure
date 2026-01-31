package pl.feature.toggle.service.configuration.project.domain.exception;

import pl.feature.toggle.service.model.project.ProjectId;

public class CannotOperateOnArchivedProjectException extends RuntimeException {

    public CannotOperateOnArchivedProjectException(ProjectId projectId) {
        super(String.format("Cannot operate on archived project: %s", projectId.uuid()));
    }
}
