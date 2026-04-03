package com.configly.structure.builder;

import com.configly.structure.environment.domain.Environment;
import com.configly.structure.environment.domain.EnvironmentType;
import com.configly.model.CreatedAt;
import com.configly.model.Revision;
import com.configly.model.UpdatedAt;
import com.configly.model.environment.EnvironmentId;
import com.configly.model.environment.EnvironmentName;
import com.configly.model.environment.EnvironmentStatus;
import com.configly.model.project.ProjectId;

public class FakeEnvironmentBuilder {
    private EnvironmentId id;
    private ProjectId projectId;
    private EnvironmentName name;
    private EnvironmentType type;
    private EnvironmentStatus status;
    private Revision revision;
    private CreatedAt createdAt;
    private UpdatedAt updatedAt;

    private FakeEnvironmentBuilder() {
        this.id = EnvironmentId.create();
        this.projectId = ProjectId.create();
        this.name = EnvironmentName.create("TEST");
        this.type = EnvironmentType.DEV;
        this.status = EnvironmentStatus.ACTIVE;
        this.revision = Revision.initialRevision();
        this.createdAt = CreatedAt.now();
        this.updatedAt = UpdatedAt.now();
    }

    public static FakeEnvironmentBuilder fakeEnvironmentBuilder() {
        return new FakeEnvironmentBuilder();
    }

    public FakeEnvironmentBuilder withId(EnvironmentId id) {
        this.id = id;
        return this;
    }

    public FakeEnvironmentBuilder withProjectId(ProjectId projectId) {
        this.projectId = projectId;
        return this;
    }

    public FakeEnvironmentBuilder withName(String name) {
        this.name = EnvironmentName.create(name);
        return this;
    }

    public FakeEnvironmentBuilder withType(EnvironmentType type) {
        this.type = type;
        return this;
    }

    public FakeEnvironmentBuilder withRevision(Revision revision) {
        this.revision = revision;
        return this;
    }

    public FakeEnvironmentBuilder withStatus(EnvironmentStatus status) {
        this.status = status;
        return this;
    }

    public Environment build() {
        return new Environment(id, projectId, name, type, status, revision, createdAt, updatedAt);
    }

}
