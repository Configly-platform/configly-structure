package pl.feature.toggle.service.configuration.builder;

import pl.feature.toggle.service.configuration.environment.application.port.in.command.ChangeEnvironmentTypeCommand;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentType;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.project.ProjectId;

public class FakeChangeEnvironmentTypeCommandBuilder {
    private ProjectId projectId;
    private EnvironmentId environmentId;
    private EnvironmentType type;

    private FakeChangeEnvironmentTypeCommandBuilder() {
        this.projectId = ProjectId.create();
        this.environmentId = EnvironmentId.create();
        this.type = EnvironmentType.DEV;
    }

    public static FakeChangeEnvironmentTypeCommandBuilder fakeChangeEnvironmentTypeCommandBuilder() {
        return new FakeChangeEnvironmentTypeCommandBuilder();
    }

    public FakeChangeEnvironmentTypeCommandBuilder withProjectId(String projectId) {
        this.projectId = ProjectId.create(projectId);
        return this;
    }

    public FakeChangeEnvironmentTypeCommandBuilder withEnvironmentId(String environmentId) {
        this.environmentId = EnvironmentId.create(environmentId);
        return this;
    }

    public FakeChangeEnvironmentTypeCommandBuilder withType(EnvironmentType type) {
        this.type = type;
        return this;
    }

    public ChangeEnvironmentTypeCommand build() {
        return new ChangeEnvironmentTypeCommand(projectId, environmentId, type);
    }

}
