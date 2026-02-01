package pl.feature.toggle.service.configuration.project.domain;

import pl.feature.toggle.service.configuration.project.domain.exception.CannotOperateOnArchivedProjectException;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;

import static pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult.ChangeSet.createChangeSet;
import static pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult.ProjectFieldChange.fieldChange;
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
            return noChanges(this);
        }
        var fieldChange = fieldChange(ProjectField.STATUS, status, ProjectStatus.ARCHIVED);
        var project = new Project(id, name, description, ProjectStatus.ARCHIVED);
        return updated(project, fieldChange);
    }

    public ProjectUpdateResult restore() {
        if (isActive()) {
            return noChanges(this);
        }
        var fieldChange = fieldChange(ProjectField.STATUS, status, ProjectStatus.ACTIVE);
        var project = new Project(id, name, description, ProjectStatus.ACTIVE);
        return updated(project, fieldChange);
    }

    public ProjectUpdateResult update(ProjectName newName, ProjectDescription newDescription) {
        if (isArchived()) {
            throw new CannotOperateOnArchivedProjectException(id);
        }
        var changeSet = createChangeSet();
        changeSet.addIfChanged(ProjectField.NAME, this.name, newName);
        changeSet.addIfChanged(ProjectField.DESCRIPTION, this.description, newDescription);

        if (changeSet.isEmpty()) {
            return noChanges(this);
        }

        var project = new Project(id, newName, newDescription, status);
        return updated(project, changeSet.toArray());
    }

    public boolean isArchived() {
        return this.status.equals(ProjectStatus.ARCHIVED);
    }

    public boolean isActive() {
        return this.status.equals(ProjectStatus.ACTIVE);
    }
}
