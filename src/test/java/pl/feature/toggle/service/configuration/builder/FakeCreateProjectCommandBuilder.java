package pl.feature.toggle.service.configuration.builder;

import pl.feature.toggle.service.configuration.project.application.port.in.command.CreateProjectCommand;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectName;

public class FakeCreateProjectCommandBuilder {

    private ProjectName name;
    private ProjectDescription description;

    private FakeCreateProjectCommandBuilder() {
        this.description = ProjectDescription.create("TEST");
        this.name = ProjectName.create("TEST");
    }

    public static FakeCreateProjectCommandBuilder createProjectCommandBuilder() {
        return new FakeCreateProjectCommandBuilder();
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
        return new CreateProjectCommand(name, description);
    }

}
