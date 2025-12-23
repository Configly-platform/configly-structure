package pl.feature.toggle.service.configuration.environment.domain;

import pl.feature.toggle.service.configuration.environment.application.port.in.CreateEnvironmentCommand;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;

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
