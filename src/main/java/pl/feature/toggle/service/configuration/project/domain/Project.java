package pl.feature.toggle.service.configuration.project.domain;

import pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult.ProjectFieldChange;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult.ProjectFieldChange.change;

public record Project(
        ProjectId id,
        ProjectName name,
        ProjectDescription description,
        Status status
) {

    public static Project create(ProjectName name, ProjectDescription description) {
        ProjectId projectId = ProjectId.create();
        return new Project(projectId, name, description, Status.ACTIVE);
    }

    public static Project create(ProjectId projectId, ProjectName name, ProjectDescription description) {
        return new Project(projectId, name, description, Status.ACTIVE);
    }

    public ProjectUpdateResult archive() {
        if (isArchived()) {
            return ProjectUpdateResult.noChanges(this);
        }
        var fieldChange = change(ProjectField.STATUS, status, Status.ARCHIVED);
        return ProjectUpdateResult.updated(new Project(id, name, description, Status.ARCHIVED), fieldChange);
    }

    public ProjectUpdateResult restore() {
        if (isActive()) {
            return ProjectUpdateResult.noChanges(this);
        }
        var fieldChange = change(ProjectField.STATUS, status, Status.ACTIVE);
        return ProjectUpdateResult.updated(new Project(id, name, description, Status.ACTIVE), fieldChange);
    }

    public boolean isArchived() {
        return this.status.equals(Status.ARCHIVED);
    }

    public boolean isActive() {
        return this.status.equals(Status.ACTIVE);
    }

    public ProjectUpdateResult update(ProjectName name, ProjectDescription description) {
        List<ProjectFieldChange> changes = new ArrayList<>();

        if (!Objects.equals(this.name, name)) {
            changes.add(change(ProjectField.NAME, this.name, name));
        }

        if (!Objects.equals(this.description, description)) {
            changes.add(change(ProjectField.DESCRIPTION, this.description, description));
        }

        var project = new Project(id, name, description, status);
        return ProjectUpdateResult.of(project, changes);
    }
}
