package pl.feature.toggle.service.configuration.builder;

import pl.feature.toggle.service.configuration.environment.application.port.in.command.ChangeEnvironmentTypeCommand;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentType;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.security.actor.Actor;
import pl.feature.toggle.service.model.security.correlation.CorrelationId;

public class FakeChangeEnvironmentTypeCommandBuilder {
    private ProjectId projectId;
    private EnvironmentId environmentId;
    private EnvironmentType type;
    private Actor actor;
    private CorrelationId correlationId;


    private FakeChangeEnvironmentTypeCommandBuilder() {
        this.projectId = ProjectId.create();
        this.environmentId = EnvironmentId.create();
        this.type = EnvironmentType.DEV;
        this.actor = Actor.system();
        this.correlationId = CorrelationId.generate();
    }

    public static FakeChangeEnvironmentTypeCommandBuilder fakeChangeEnvironmentTypeCommandBuilder() {
        return new FakeChangeEnvironmentTypeCommandBuilder();
    }

    public FakeChangeEnvironmentTypeCommandBuilder withProjectId(String projectId) {
        this.projectId = ProjectId.create(projectId);
        return this;
    }

    public FakeChangeEnvironmentTypeCommandBuilder withActor(Actor actor) {
        this.actor = actor;
        return this;
    }

    public FakeChangeEnvironmentTypeCommandBuilder withCorrelationId(String correlationId) {
        this.correlationId = CorrelationId.of(correlationId);
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
        return new ChangeEnvironmentTypeCommand(projectId, environmentId, type, actor, correlationId);
    }

}
