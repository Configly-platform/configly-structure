package pl.feature.toggle.service.configuration.project.application.port.in.command;

import pl.feature.toggle.service.configuration.project.domain.Status;
import pl.feature.toggle.service.model.project.ProjectId;

public record ChangeStatusCommand(
        ProjectId projectId,
        Status newStatus
) {
    public static ChangeStatusCommand of(String projectId, String status) {
        return new ChangeStatusCommand(ProjectId.create(projectId), Status.valueOf(status));
    }
}
