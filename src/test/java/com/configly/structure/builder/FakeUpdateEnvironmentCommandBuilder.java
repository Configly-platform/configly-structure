package com.configly.structure.builder;

import com.configly.structure.environment.application.port.in.command.UpdateEnvironmentCommand;
import com.configly.model.environment.EnvironmentId;
import com.configly.model.environment.EnvironmentName;
import com.configly.model.project.ProjectId;
import com.configly.web.model.actor.Actor;
import com.configly.web.model.correlation.CorrelationId;

public class FakeUpdateEnvironmentCommandBuilder {

    private EnvironmentId environmentId;
    private ProjectId projectId;
    private EnvironmentName name;
    private Actor actor;
    private CorrelationId correlationId;

    private FakeUpdateEnvironmentCommandBuilder() {
        this.environmentId = EnvironmentId.create();
        this.projectId = ProjectId.create();
        this.name = EnvironmentName.create("TEST");
        this.actor = Actor.system();
        this.correlationId = CorrelationId.generate();
    }

    public static FakeUpdateEnvironmentCommandBuilder fakeUpdateEnvironmentCommandBuilder() {
        return new FakeUpdateEnvironmentCommandBuilder();
    }

    public FakeUpdateEnvironmentCommandBuilder withEnvironmentId(EnvironmentId environmentId) {
        this.environmentId = environmentId;
        return this;
    }

    public FakeUpdateEnvironmentCommandBuilder withActor(Actor actor) {
        this.actor = actor;
        return this;
    }

    public FakeUpdateEnvironmentCommandBuilder withCorrelationId(String correlationId) {
        this.correlationId = CorrelationId.of(correlationId);
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
        return new UpdateEnvironmentCommand(environmentId, projectId, name, actor, correlationId);
    }
}
