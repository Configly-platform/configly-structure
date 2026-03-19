package pl.feature.toggle.service.configuration.builder;

import pl.feature.toggle.service.configuration.project.application.port.in.command.UpdateProjectCommand;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;
import pl.feature.toggle.service.web.actor.Actor;
import pl.feature.toggle.service.web.correlation.CorrelationId;

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
