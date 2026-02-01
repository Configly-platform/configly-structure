package pl.feature.toggle.service.configuration.builder;

import pl.feature.toggle.service.configuration.environment.application.port.in.command.CreateEnvironmentCommand;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentType;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;

import java.util.UUID;

public class FakeCreateEnvironmentCommandBuilder {

    private EnvironmentName name;
    private ProjectId projectId;
    private EnvironmentType type;

    private FakeCreateEnvironmentCommandBuilder() {
        this.name = EnvironmentName.create("TEST");
        this.projectId = ProjectId.create();
        this.type = EnvironmentType.DEV;
    }

    public static FakeCreateEnvironmentCommandBuilder createEnvironmentCommandBuilder() {
        return new FakeCreateEnvironmentCommandBuilder();
    }

    public FakeCreateEnvironmentCommandBuilder withName(String name) {
        this.name = EnvironmentName.create(name);
        return this;
    }

    public FakeCreateEnvironmentCommandBuilder withType(EnvironmentType type) {
        this.type = type;
        return this;
    }

    public FakeCreateEnvironmentCommandBuilder withProjectId(UUID projectId) {
        this.projectId = ProjectId.create(projectId);
        return this;
    }

    public FakeCreateEnvironmentCommandBuilder withProjectId(ProjectId projectId) {
        this.projectId = projectId;
        return this;
    }

    public CreateEnvironmentCommand build() {
        return new CreateEnvironmentCommand(name, projectId, type);
    }
}
