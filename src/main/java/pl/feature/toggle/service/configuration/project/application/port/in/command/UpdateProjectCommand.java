package pl.feature.toggle.service.configuration.project.application.port.in.command;

import pl.feature.toggle.service.configuration.project.infrastructure.in.rest.dto.ProjectSnapshotDto;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;

public record UpdateProjectCommand(
        ProjectId projectId,
        ProjectName name,
        ProjectDescription description
) {

    public static UpdateProjectCommand from(String projectId, ProjectSnapshotDto dto) {
        return new UpdateProjectCommand(ProjectId.create(projectId), ProjectName.create(dto.name()), ProjectDescription.create(dto.description()));
    }
}
