package pl.feature.toggle.service.configuration.builder;

import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.model.CreatedAt;
import pl.feature.toggle.service.model.Revision;
import pl.feature.toggle.service.model.UpdatedAt;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;
import pl.feature.toggle.service.model.project.ProjectStatus;

public class FakeProjectBuilder {
    private ProjectId id;
    private ProjectName name;
    private ProjectDescription description;
    private ProjectStatus status;
    private Revision revision;
    private CreatedAt createdAt;
    private UpdatedAt updatedAt;

    private FakeProjectBuilder() {
        this.id = ProjectId.create();
        this.name = ProjectName.create("TEST");
        this.description = ProjectDescription.create("TEST");
        this.status = ProjectStatus.ACTIVE;
        this.revision = Revision.initialRevision();
        this.createdAt = CreatedAt.now();
        this.updatedAt = UpdatedAt.now();
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

    public FakeProjectBuilder withRevision(Revision revision) {
        this.revision = revision;
        return this;
    }

    public Project build() {
        return new Project(id, name, description, status, revision, createdAt, updatedAt);
    }
}
