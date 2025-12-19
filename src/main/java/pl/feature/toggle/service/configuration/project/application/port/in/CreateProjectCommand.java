package pl.feature.toggle.service.configuration.project.application.port.in;

import com.ftaas.domain.project.ProjectDescription;
import com.ftaas.domain.project.ProjectName;
import pl.feature.toggle.service.configuration.project.infrastructure.in.rest.dto.CreateProjectDto;

public record CreateProjectCommand(
        ProjectName name,
        ProjectDescription description
) {

    public static CreateProjectCommand from(CreateProjectDto dto) {
        return new CreateProjectCommand(ProjectName.create(dto.name()), ProjectDescription.create(dto.description()));
    }

    public static CreateProjectCommand create(String name, String description) {
        return new CreateProjectCommand(ProjectName.create(name), ProjectDescription.create(description));
    }

}
