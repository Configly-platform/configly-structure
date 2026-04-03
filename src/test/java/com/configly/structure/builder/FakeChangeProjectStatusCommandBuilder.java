package com.configly.structure.builder;

import com.configly.structure.project.application.port.in.command.ChangeProjectStatusCommand;
import com.configly.model.project.ProjectId;
import com.configly.model.project.ProjectStatus;
import com.configly.web.actor.Actor;
import com.configly.web.correlation.CorrelationId;

public class FakeChangeProjectStatusCommandBuilder {

    private ProjectId projectId;
    private ProjectStatus projectStatus;
    private Actor actor;
    private CorrelationId correlationId;

    private FakeChangeProjectStatusCommandBuilder() {
        this.projectId = ProjectId.create();
        this.projectStatus = ProjectStatus.ACTIVE;
        this.actor = Actor.system();
        this.correlationId = CorrelationId.generate();
    }

    public static FakeChangeProjectStatusCommandBuilder fakeChangeProjectStatusCommandBuilder() {
        return new FakeChangeProjectStatusCommandBuilder();
    }

    public FakeChangeProjectStatusCommandBuilder withActor(Actor actor) {
        this.actor = actor;
        return this;
    }

    public FakeChangeProjectStatusCommandBuilder withCorrelationId(String correlationId) {
        this.correlationId = CorrelationId.of(correlationId);
        return this;
    }


    public FakeChangeProjectStatusCommandBuilder withProjectId(ProjectId projectId) {
        this.projectId = projectId;
        return this;
    }

    public FakeChangeProjectStatusCommandBuilder withProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
        return this;
    }

    public ChangeProjectStatusCommand build() {
        return new ChangeProjectStatusCommand(projectId, projectStatus, actor, correlationId);
    }
}
