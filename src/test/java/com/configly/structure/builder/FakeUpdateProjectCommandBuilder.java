package com.configly.structure.builder;

import com.configly.structure.project.application.port.in.command.UpdateProjectCommand;
import com.configly.model.project.ProjectDescription;
import com.configly.model.project.ProjectId;
import com.configly.model.project.ProjectName;
import com.configly.web.model.actor.Actor;
import com.configly.web.model.correlation.CorrelationId;

public class FakeUpdateProjectCommandBuilder {

    private ProjectId projectId;
    private ProjectName name;
    private ProjectDescription description;
    private Actor actor;
    private CorrelationId correlationId;

    private FakeUpdateProjectCommandBuilder() {
        this.projectId = ProjectId.create();
        this.name = ProjectName.create("TEST");
        this.description = ProjectDescription.create("TEST");
        this.actor = Actor.system();
        this.correlationId = CorrelationId.generate();
    }

    public static FakeUpdateProjectCommandBuilder fakeUpdateProjectCommandBuilder() {
        return new FakeUpdateProjectCommandBuilder();
    }

    public FakeUpdateProjectCommandBuilder withActor(Actor actor) {
        this.actor = actor;
        return this;
    }

    public FakeUpdateProjectCommandBuilder withCorrelationId(String correlationId) {
        this.correlationId = CorrelationId.of(correlationId);
        return this;
    }

    public FakeUpdateProjectCommandBuilder withProjectId(ProjectId projectId) {
        this.projectId = projectId;
        return this;
    }

    public FakeUpdateProjectCommandBuilder withName(String name) {
        this.name = ProjectName.create(name);
        return this;
    }

    public FakeUpdateProjectCommandBuilder withDescription(String description) {
        this.description = ProjectDescription.create(description);
        return this;
    }

    public UpdateProjectCommand build() {
        return new UpdateProjectCommand(projectId, name, description, actor, correlationId);
    }

}
