package com.configly.structure.builder;

import com.configly.structure.project.application.port.in.command.CreateProjectCommand;
import com.configly.model.project.ProjectDescription;
import com.configly.model.project.ProjectName;
import com.configly.web.model.actor.Actor;
import com.configly.web.model.correlation.CorrelationId;

public class FakeCreateProjectCommandBuilder {

    private ProjectName name;
    private ProjectDescription description;
    private Actor actor;
    private CorrelationId correlationId;

    private FakeCreateProjectCommandBuilder() {
        this.description = ProjectDescription.create("TEST");
        this.name = ProjectName.create("TEST");
        this.actor = Actor.system();
        this.correlationId = CorrelationId.generate();
    }

    public static FakeCreateProjectCommandBuilder createProjectCommandBuilder() {
        return new FakeCreateProjectCommandBuilder();
    }

    public FakeCreateProjectCommandBuilder withActor(Actor actor) {
        this.actor = actor;
        return this;
    }

    public FakeCreateProjectCommandBuilder withCorrelationId(String correlationId) {
        this.correlationId = CorrelationId.of(correlationId);
        return this;
    }

    public FakeCreateProjectCommandBuilder withName(String name) {
        this.name = ProjectName.create(name);
        return this;
    }

    public FakeCreateProjectCommandBuilder withDescription(String description) {
        this.description = ProjectDescription.create(description);
        return this;
    }

    public CreateProjectCommand build() {
        return new CreateProjectCommand(name, description, actor, correlationId);
    }

}
