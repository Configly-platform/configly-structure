package pl.feature.toggle.service.configuration.project.domain;

import pl.feature.toggle.service.configuration.project.domain.exception.CannotOperateOnArchivedProjectException;
import pl.feature.toggle.service.model.CreatedAt;
import pl.feature.toggle.service.model.Revision;
import pl.feature.toggle.service.model.UpdatedAt;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;
import pl.feature.toggle.service.model.project.ProjectStatus;

import static pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult.ChangeSet.createChangeSet;
import static pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult.ProjectFieldChange.fieldChange;
import static pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult.noChanges;
import static pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult.updated;
import static pl.feature.toggle.service.model.Revision.initialRevision;

public record Project(
        ProjectId id,
        ProjectName name,
        ProjectDescription description,
        ProjectStatus status,
        Revision revision,
        CreatedAt createdAt,
        UpdatedAt updatedAt
) {

    public static Project create(ProjectName name, ProjectDescription description) {
        ProjectId projectId = ProjectId.create();
        return new Project(projectId, name, description, ProjectStatus.ACTIVE, initialRevision(),
                CreatedAt.now(), UpdatedAt.now());
    }

    public static Project create(ProjectId projectId, ProjectName name, ProjectDescription description) {
        return new Project(projectId, name, description, ProjectStatus.ACTIVE, initialRevision(),
                CreatedAt.now(), UpdatedAt.now());
    }

    public ProjectUpdateResult archive() {
        if (isArchived()) {
            return noChanges(this);
        }
        var fieldChange = fieldChange(ProjectField.STATUS, status, ProjectStatus.ARCHIVED);
        var project = new Project(id,
                name,
                description,
                ProjectStatus.ARCHIVED,
                revision.next(),
                createdAt,
                UpdatedAt.now());
        return updated(project, revision, fieldChange);
    }

    public ProjectUpdateResult restore() {
        if (isActive()) {
            return noChanges(this);
        }
        var fieldChange = fieldChange(ProjectField.STATUS, status, ProjectStatus.ACTIVE);
        var project = new Project(id,
                name,
                description,
                ProjectStatus.ACTIVE,
                revision.next(),
                createdAt,
                UpdatedAt.now());
        return updated(project, revision, fieldChange);
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

        var project = new Project(id,
                newName,
                newDescription,
                status,
                revision.next(),
                createdAt,
                UpdatedAt.now());
        return updated(project, revision, changeSet.toArray());
    }

    public boolean isArchived() {
        return this.status.equals(ProjectStatus.ARCHIVED);
    }

    public boolean isActive() {
        return this.status.equals(ProjectStatus.ACTIVE);
    }
}
