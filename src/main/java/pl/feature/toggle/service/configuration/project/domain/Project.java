package pl.feature.toggle.service.configuration.project.domain;

import pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult.ProjectFieldChange;
import pl.feature.toggle.service.configuration.project.domain.exception.CannotOperateOnArchivedProjectException;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult.ProjectFieldChange.change;
import static pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult.noChanges;
import static pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult.updated;

public record Project(
        ProjectId id,
        ProjectName name,
        ProjectDescription description,
        ProjectStatus status
) {

    public static Project create(ProjectName name, ProjectDescription description) {
        ProjectId projectId = ProjectId.create();
        return new Project(projectId, name, description, ProjectStatus.ACTIVE);
    }

    public static Project create(ProjectId projectId, ProjectName name, ProjectDescription description) {
        return new Project(projectId, name, description, ProjectStatus.ACTIVE);
    }

    public ProjectUpdateResult archive() {
        if (isArchived()) {
            return noChanges();
        }
        var fieldChange = change(ProjectField.STATUS, status, ProjectStatus.ARCHIVED);
        return updated(new Project(id, name, description, ProjectStatus.ARCHIVED), fieldChange);
    }

    public ProjectUpdateResult restore() {
        if (isActive()) {
            return noChanges();
        }
        var fieldChange = change(ProjectField.STATUS, status, ProjectStatus.ACTIVE);
        return updated(new Project(id, name, description, ProjectStatus.ACTIVE), fieldChange);
    }

    public boolean isArchived() {
        return this.status.equals(ProjectStatus.ARCHIVED);
    }

    public boolean isActive() {
        return this.status.equals(ProjectStatus.ACTIVE);
    }

    public ProjectUpdateResult update(ProjectName name, ProjectDescription description) {
        if (isArchived()) {
            throw new CannotOperateOnArchivedProjectException(id);
        }
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
