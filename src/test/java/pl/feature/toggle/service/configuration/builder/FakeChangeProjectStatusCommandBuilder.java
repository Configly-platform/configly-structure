package pl.feature.toggle.service.configuration.builder;

import pl.feature.toggle.service.configuration.project.application.port.in.command.ChangeProjectStatusCommand;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectStatus;

public class FakeChangeProjectStatusCommandBuilder {

    private ProjectId projectId;
    private ProjectStatus projectStatus;

    private FakeChangeProjectStatusCommandBuilder() {
        this.projectId = ProjectId.create();
        this.projectStatus = ProjectStatus.ACTIVE;
    }

    public static FakeChangeProjectStatusCommandBuilder fakeChangeProjectStatusCommandBuilder() {
        return new FakeChangeProjectStatusCommandBuilder();
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
        return new ChangeProjectStatusCommand(projectId, projectStatus);
    }
}
