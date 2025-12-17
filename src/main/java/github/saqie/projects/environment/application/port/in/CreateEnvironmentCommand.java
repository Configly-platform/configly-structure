package github.saqie.projects.environment.application.port.in;

import com.ftaas.domain.environment.EnvironmentName;
import com.ftaas.domain.project.ProjectId;
import github.saqie.projects.environment.infrastructure.in.rest.dto.CreateEnvironmentDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateEnvironmentCommand(
        EnvironmentName name,
        ProjectId projectId
) {

    public static CreateEnvironmentCommand from(CreateEnvironmentDto dto) {
        return new CreateEnvironmentCommand(EnvironmentName.create(dto.name()), ProjectId.create(dto.projectId()));
    }

    public static CreateEnvironmentCommand create(String name, UUID projectId) {
        return new CreateEnvironmentCommand(EnvironmentName.create(name), ProjectId.create(projectId));
    }

}
