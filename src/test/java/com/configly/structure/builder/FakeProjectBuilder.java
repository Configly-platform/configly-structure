package com.configly.structure.builder;

import com.configly.structure.project.domain.Project;
import com.configly.model.CreatedAt;
import com.configly.model.Revision;
import com.configly.model.UpdatedAt;
import com.configly.model.project.ProjectDescription;
import com.configly.model.project.ProjectId;
import com.configly.model.project.ProjectName;
import com.configly.model.project.ProjectStatus;

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
