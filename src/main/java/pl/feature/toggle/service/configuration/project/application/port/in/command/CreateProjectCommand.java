package pl.feature.toggle.service.configuration.project.application.port.in.command;

import pl.feature.toggle.service.configuration.project.infrastructure.in.rest.dto.ProjectSnapshotDto;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectName;

public record CreateProjectCommand(
        ProjectName name,
        ProjectDescription description
) {

    public static CreateProjectCommand from(ProjectSnapshotDto dto) {
        return new CreateProjectCommand(ProjectName.create(dto.name()), ProjectDescription.create(dto.description()));
    }

    public static CreateProjectCommand create(String name, String description) {
        return new CreateProjectCommand(ProjectName.create(name), ProjectDescription.create(description));
    }

}
