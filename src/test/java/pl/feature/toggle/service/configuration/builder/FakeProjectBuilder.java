package pl.feature.toggle.service.configuration.builder;

import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.configuration.project.domain.ProjectStatus;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;

public class FakeProjectBuilder {
    private ProjectId id;
    private ProjectName name;
    private ProjectDescription description;
    private ProjectStatus status;

    private FakeProjectBuilder() {
        this.id = ProjectId.create();
        this.name = ProjectName.create("TEST");
        this.description = ProjectDescription.create("TEST");
        this.status = ProjectStatus.ACTIVE;
    }

    public static FakeProjectBuilder fakeProjectBuilder() {
        return new FakeProjectBuilder();
    }

    public FakeProjectBuilder withId(ProjectId id) {
        this.id = id;
        return this;
    }

    public FakeProjectBuilder withName(ProjectName name) {
        this.name = name;
        return this;
    }

    public FakeProjectBuilder withDescription(ProjectDescription description) {
        this.description = description;
        return this;
    }

    public FakeProjectBuilder withStatus(ProjectStatus status) {
        this.status = status;
        return this;
    }

    public Project build() {
        return new Project(id, name, description, status);
    }
}
