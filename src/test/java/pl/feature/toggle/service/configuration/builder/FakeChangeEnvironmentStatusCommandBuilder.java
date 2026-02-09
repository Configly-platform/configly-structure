package pl.feature.toggle.service.configuration.builder;

import pl.feature.toggle.service.configuration.environment.application.port.in.command.ChangeEnvironmentStatusCommand;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentStatus;
import pl.feature.toggle.service.model.project.ProjectId;

public class FakeChangeEnvironmentStatusCommandBuilder {
    private ProjectId projectId;
    private EnvironmentId environmentId;
    private EnvironmentStatus newEnvironmentStatus;

    private FakeChangeEnvironmentStatusCommandBuilder() {
        this.projectId = ProjectId.create();
        this.environmentId = EnvironmentId.create();
        this.newEnvironmentStatus = EnvironmentStatus.ACTIVE;
    }

    public static FakeChangeEnvironmentStatusCommandBuilder fakeChangeEnvironmentStatusCommandBuilder() {
        return new FakeChangeEnvironmentStatusCommandBuilder();
    }

    public FakeChangeEnvironmentStatusCommandBuilder withProjectId(String projectId) {
        this.projectId = ProjectId.create(projectId);
        return this;
    }

    public FakeChangeEnvironmentStatusCommandBuilder withEnvironmentId(String environmentId) {
        this.environmentId = EnvironmentId.create(environmentId);
        return this;
    }

    public FakeChangeEnvironmentStatusCommandBuilder withNewEnvironmentStatus(EnvironmentStatus newEnvironmentStatus) {
        this.newEnvironmentStatus = newEnvironmentStatus;
        return this;
    }

    public ChangeEnvironmentStatusCommand build() {
        return new ChangeEnvironmentStatusCommand(projectId, environmentId, newEnvironmentStatus);
    }
}
