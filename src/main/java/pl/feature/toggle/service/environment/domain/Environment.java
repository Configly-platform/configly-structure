package pl.feature.toggle.service.environment.domain;

import com.ftaas.domain.environment.EnvironmentId;
import com.ftaas.domain.environment.EnvironmentName;
import com.ftaas.domain.project.ProjectId;
import pl.feature.toggle.service.environment.application.port.in.CreateEnvironmentCommand;

public record Environment(
        EnvironmentId id,
        ProjectId projectId,
        EnvironmentName name
) {

    public static Environment create(CreateEnvironmentCommand command) {
        EnvironmentId environmentId = EnvironmentId.create();
        return new Environment(environmentId, command.projectId(), command.name());
    }

    public static Environment create(ProjectId projectId, EnvironmentName name) {
        EnvironmentId environmentId = EnvironmentId.create();
        return new Environment(environmentId, projectId, name);
    }

    public static Environment create(EnvironmentId environmentId, ProjectId projectId, EnvironmentName name) {
        return new Environment(environmentId, projectId, name);
    }

}
