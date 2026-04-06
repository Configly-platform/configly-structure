package com.configly.structure.builder;

import com.configly.structure.environment.application.port.in.command.ChangeEnvironmentStatusCommand;
import com.configly.model.environment.EnvironmentId;
import com.configly.model.environment.EnvironmentStatus;
import com.configly.model.project.ProjectId;
import com.configly.web.model.actor.Actor;
import com.configly.web.model.correlation.CorrelationId;

public class FakeChangeEnvironmentStatusCommandBuilder {
    private ProjectId projectId;
    private EnvironmentId environmentId;
    private EnvironmentStatus newEnvironmentStatus;
    private Actor actor;
    private CorrelationId correlationId;

    private FakeChangeEnvironmentStatusCommandBuilder() {
        this.projectId = ProjectId.create();
        this.environmentId = EnvironmentId.create();
        this.newEnvironmentStatus = EnvironmentStatus.ACTIVE;
        this.actor = Actor.system();
        this.correlationId = CorrelationId.generate();
    }

    public static FakeChangeEnvironmentStatusCommandBuilder fakeChangeEnvironmentStatusCommandBuilder() {
        return new FakeChangeEnvironmentStatusCommandBuilder();
    }

    public FakeChangeEnvironmentStatusCommandBuilder withProjectId(String projectId) {
        this.projectId = ProjectId.create(projectId);
        return this;
    }

    public FakeChangeEnvironmentStatusCommandBuilder withActor(Actor actor) {
        this.actor = actor;
        return this;
    }

    public FakeChangeEnvironmentStatusCommandBuilder withCorrelationId(String correlationId) {
        this.correlationId = CorrelationId.of(correlationId);
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
        return new ChangeEnvironmentStatusCommand(projectId, environmentId, newEnvironmentStatus, actor, correlationId);
    }
}
