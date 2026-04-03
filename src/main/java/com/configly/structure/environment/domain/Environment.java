package com.configly.structure.environment.domain;

import com.configly.structure.environment.application.port.in.command.CreateEnvironmentCommand;
import com.configly.structure.environment.domain.exception.CannotOperateOnArchivedEnvironmentException;
import com.configly.model.CreatedAt;
import com.configly.model.Revision;
import com.configly.model.UpdatedAt;
import com.configly.model.environment.EnvironmentId;
import com.configly.model.environment.EnvironmentName;
import com.configly.model.environment.EnvironmentStatus;
import com.configly.model.project.ProjectId;

import static com.configly.structure.environment.domain.EnvironmentUpdateResult.ChangeSet.createChangeSet;
import static com.configly.structure.environment.domain.EnvironmentUpdateResult.EnvironmentFieldChange.fieldChange;
import static com.configly.structure.environment.domain.EnvironmentUpdateResult.noChanges;
import static com.configly.structure.environment.domain.EnvironmentUpdateResult.updated;
import static com.configly.model.Revision.initialRevision;

public record Environment(
        EnvironmentId id,
        ProjectId projectId,
        EnvironmentName name,
        EnvironmentType type,
        EnvironmentStatus status,
        Revision revision,
        CreatedAt createdAt,
        UpdatedAt updatedAt
) {

    public static Environment create(CreateEnvironmentCommand command) {
        return new Environment(EnvironmentId.create(), command.projectId(), command.name(), command.type(),
                EnvironmentStatus.ACTIVE, initialRevision(), CreatedAt.now(), UpdatedAt.now());
    }

    public static Environment create(ProjectId projectId, EnvironmentName name, EnvironmentType type) {
        return new Environment(EnvironmentId.create(), projectId, name, type,
                EnvironmentStatus.ACTIVE, initialRevision(), CreatedAt.now(), UpdatedAt.now());
    }

    public EnvironmentUpdateResult archive() {
        if (isArchived()) {
            return noChanges(this);
        }
        var environment = new Environment(id,
                projectId,
                name,
                type,
                EnvironmentStatus.ARCHIVED,
                revision.next(),
                createdAt,
                UpdatedAt.now());
        var change = fieldChange(EnvironmentField.STATUS, status, EnvironmentStatus.ARCHIVED);
        return updated(environment, revision, change);
    }

    public EnvironmentUpdateResult restore() {
        if (isActive()) {
            return noChanges(this);
        }
        var environment = new Environment(id,
                projectId,
                name,
                type,
                EnvironmentStatus.ACTIVE,
                revision.next(),
                createdAt,
                UpdatedAt.now());
        var change = fieldChange(EnvironmentField.STATUS, status, EnvironmentStatus.ACTIVE);
        return updated(environment, revision, change);
    }

    public EnvironmentUpdateResult changeType(EnvironmentType newType) {
        if (isArchived()) {
            throw new CannotOperateOnArchivedEnvironmentException(id);
        }
        var changeSet = createChangeSet();
        changeSet.addIfChanged(EnvironmentField.TYPE, type, newType);

        if (changeSet.isEmpty()) {
            return noChanges(this);
        }

        var environment = new Environment(id,
                projectId,
                name,
                newType,
                status,
                revision.next(),
                createdAt,
                UpdatedAt.now());
        return updated(environment, revision, changeSet.toArray());
    }

    public EnvironmentUpdateResult update(EnvironmentName newName) {
        if (isArchived()) {
            throw new CannotOperateOnArchivedEnvironmentException(id);
        }
        var changeSet = createChangeSet();
        changeSet.addIfChanged(EnvironmentField.NAME, name, newName);

        if (changeSet.isEmpty()) {
            return noChanges(this);
        }

        var environment = new Environment(id,
                projectId,
                newName,
                type,
                status,
                revision.next(),
                createdAt,
                UpdatedAt.now());
        return updated(environment, revision, changeSet.toArray());
    }

    public boolean isArchived() {
        return status.equals(EnvironmentStatus.ARCHIVED);
    }

    public boolean isActive() {
        return status.equals(EnvironmentStatus.ACTIVE);
    }

}
