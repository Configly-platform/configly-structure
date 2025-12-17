package github.saqie.projects.builder;

import github.saqie.projects.project.application.port.in.CreateProjectCommand;

public class FakeCreateProjectCommandBuilder {

    private String name;
    private String description;

    private FakeCreateProjectCommandBuilder() {
        this.description = "TEST";
        this.name = "TEST";
    }

    public static FakeCreateProjectCommandBuilder createProjectCommandBuilder() {
        return new FakeCreateProjectCommandBuilder();
    }

    public FakeCreateProjectCommandBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public FakeCreateProjectCommandBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public CreateProjectCommand build() {
        return CreateProjectCommand.create(name, description);
    }

}
