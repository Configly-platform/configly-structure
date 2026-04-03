package com.configly.structure.project.domain;

import com.configly.structure.project.domain.exception.CannotOperateOnArchivedProjectException;
import com.configly.model.CreatedAt;
import com.configly.model.Revision;
import com.configly.model.UpdatedAt;
import com.configly.model.project.ProjectDescription;
import com.configly.model.project.ProjectId;
import com.configly.model.project.ProjectName;
import com.configly.model.project.ProjectStatus;

import static com.configly.structure.project.domain.ProjectUpdateResult.ChangeSet.createChangeSet;
import static com.configly.structure.project.domain.ProjectUpdateResult.ProjectFieldChange.fieldChange;
import static com.configly.structure.project.domain.ProjectUpdateResult.noChanges;
import static com.configly.structure.project.domain.ProjectUpdateResult.updated;
import static com.configly.model.Revision.initialRevision;

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
