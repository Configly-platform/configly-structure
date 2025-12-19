package pl.feature.toggle.service.configuration.builder;

import com.ftaas.domain.project.ProjectId;
import pl.feature.toggle.service.configuration.environment.application.port.in.CreateEnvironmentCommand;

import java.util.UUID;

public class FakeCreateEnvironmentCommandBuilder {

    private String name;
    private UUID projectId;

    private FakeCreateEnvironmentCommandBuilder() {
        this.name = "TEST";
        this.projectId = UUID.randomUUID();
    }

    public static FakeCreateEnvironmentCommandBuilder createEnvironmentCommandBuilder() {
        return new FakeCreateEnvironmentCommandBuilder();
    }

    public FakeCreateEnvironmentCommandBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public FakeCreateEnvironmentCommandBuilder withProjectId(UUID projectId) {
        this.projectId = projectId;
        return this;
    }

    public FakeCreateEnvironmentCommandBuilder withProjectId(ProjectId projectId) {
        this.projectId = projectId.uuid();
        return this;
    }

    public CreateEnvironmentCommand build() {
        return CreateEnvironmentCommand.create(name, projectId);
    }
}
