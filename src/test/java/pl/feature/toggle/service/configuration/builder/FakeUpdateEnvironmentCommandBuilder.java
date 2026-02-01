package pl.feature.toggle.service.configuration.builder;

import pl.feature.toggle.service.configuration.environment.application.port.in.command.UpdateEnvironmentCommand;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;

public class FakeUpdateEnvironmentCommandBuilder {

    private EnvironmentId environmentId;
    private ProjectId projectId;
    private EnvironmentName name;

    private FakeUpdateEnvironmentCommandBuilder() {
        this.environmentId = EnvironmentId.create();
        this.projectId = ProjectId.create();
        this.name = EnvironmentName.create("TEST");
    }

    public static FakeUpdateEnvironmentCommandBuilder fakeUpdateEnvironmentCommandBuilder() {
        return new FakeUpdateEnvironmentCommandBuilder();
    }

    public FakeUpdateEnvironmentCommandBuilder withEnvironmentId(EnvironmentId environmentId) {
        this.environmentId = environmentId;
        return this;
    }

    public FakeUpdateEnvironmentCommandBuilder withProjectId(ProjectId projectId) {
        this.projectId = projectId;
        return this;
    }

    public FakeUpdateEnvironmentCommandBuilder withName(EnvironmentName name) {
        this.name = name;
        return this;
    }

    public UpdateEnvironmentCommand build() {
        return new UpdateEnvironmentCommand(environmentId, projectId, name);
    }
}
