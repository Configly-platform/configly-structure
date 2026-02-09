package pl.feature.toggle.service.configuration.project.application.port.in.command;

import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectStatus;

public record ChangeProjectStatusCommand(
        ProjectId projectId,
        ProjectStatus newProjectStatus
) {
    public static ChangeProjectStatusCommand of(String projectId, String status) {
        return new ChangeProjectStatusCommand(ProjectId.create(projectId), ProjectStatus.valueOf(status));
    }
}
