package com.configly.structure.builder;

import com.configly.structure.environment.application.port.in.command.CreateEnvironmentCommand;
import com.configly.structure.environment.domain.EnvironmentType;
import com.configly.model.environment.EnvironmentName;
import com.configly.model.project.ProjectId;
import com.configly.web.actor.Actor;
import com.configly.web.correlation.CorrelationId;

import java.util.UUID;

public class FakeCreateEnvironmentCommandBuilder {

    private EnvironmentName name;
    private ProjectId projectId;
    private EnvironmentType type;
    private Actor actor;
    private CorrelationId correlationId;

    private FakeCreateEnvironmentCommandBuilder() {
        this.name = EnvironmentName.create("TEST");
        this.projectId = ProjectId.create();
        this.type = EnvironmentType.DEV;
        this.actor = Actor.system();
        this.correlationId = CorrelationId.generate();
    }

    public static FakeCreateEnvironmentCommandBuilder createEnvironmentCommandBuilder() {
        return new FakeCreateEnvironmentCommandBuilder();
    }

    public FakeCreateEnvironmentCommandBuilder withName(String name) {
        this.name = EnvironmentName.create(name);
        return this;
    }

    public FakeCreateEnvironmentCommandBuilder withActor(Actor actor) {
        this.actor = actor;
        return this;
    }

    public FakeCreateEnvironmentCommandBuilder withCorrelationId(String correlationId) {
        this.correlationId = CorrelationId.of(correlationId);
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
        return new CreateEnvironmentCommand(name, projectId, type, actor, correlationId);
    }
}
