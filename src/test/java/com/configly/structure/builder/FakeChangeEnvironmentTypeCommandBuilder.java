package com.configly.structure.builder;

import com.configly.structure.environment.application.port.in.command.ChangeEnvironmentTypeCommand;
import com.configly.structure.environment.domain.EnvironmentType;
import com.configly.model.environment.EnvironmentId;
import com.configly.model.project.ProjectId;
import com.configly.web.model.actor.Actor;
import com.configly.web.model.correlation.CorrelationId;
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
