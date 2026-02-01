package pl.feature.toggle.service.configuration.builder;

import pl.feature.toggle.service.configuration.project.application.port.in.command.UpdateProjectCommand;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;

public class FakeUpdateProjectCommandBuilder {

    private ProjectId projectId;
    private ProjectName name;
    private ProjectDescription description;

    private FakeUpdateProjectCommandBuilder() {
        this.projectId = ProjectId.create();
        this.name = ProjectName.create("TEST");
        this.description = ProjectDescription.create("TEST");
    }

    public static FakeUpdateProjectCommandBuilder fakeUpdateProjectCommandBuilder() {
        return new FakeUpdateProjectCommandBuilder();
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
        return new UpdateProjectCommand(projectId, name, description);
    }

}
