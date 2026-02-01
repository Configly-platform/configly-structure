package pl.feature.toggle.service.configuration.environment.domain;

import pl.feature.toggle.service.configuration.environment.application.port.in.command.CreateEnvironmentCommand;
import pl.feature.toggle.service.configuration.environment.domain.exception.CannotOperateOnArchivedEnvironmentException;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;

import static pl.feature.toggle.service.configuration.environment.domain.EnvironmentUpdateResult.ChangeSet.createChangeSet;
import static pl.feature.toggle.service.configuration.environment.domain.EnvironmentUpdateResult.EnvironmentFieldChange.fieldChange;
import static pl.feature.toggle.service.configuration.environment.domain.EnvironmentUpdateResult.noChanges;
import static pl.feature.toggle.service.configuration.environment.domain.EnvironmentUpdateResult.updated;

public record Environment(
        EnvironmentId id,
        ProjectId projectId,
        EnvironmentName name,
        EnvironmentType type,
        EnvironmentStatus status
) {

    public static Environment create(CreateEnvironmentCommand command) {
        return new Environment(EnvironmentId.create(), command.projectId(), command.name(), command.type(), EnvironmentStatus.ACTIVE);
    }

    public static Environment create(ProjectId projectId, EnvironmentName name, EnvironmentType type) {
        return new Environment(EnvironmentId.create(), projectId, name, type, EnvironmentStatus.ACTIVE);
    }

    public EnvironmentUpdateResult archive() {
        if (isArchived()) {
            return noChanges(this);
        }
        var environment = new Environment(id, projectId, name, type, EnvironmentStatus.ARCHIVED);
        var change = fieldChange(EnvironmentField.STATUS, status, EnvironmentStatus.ARCHIVED);
        return updated(environment, change);
    }

    public EnvironmentUpdateResult restore() {
        if (isActive()) {
            return noChanges(this);
        }
        var environment = new Environment(id, projectId, name, type, EnvironmentStatus.ACTIVE);
        var change = fieldChange(EnvironmentField.STATUS, status, EnvironmentStatus.ARCHIVED);
        return updated(environment, change);
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

        var environment = new Environment(id, projectId, name, newType, status);
        return updated(environment, changeSet.toArray());
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

        var environment = new Environment(id, projectId, newName, type, status);
        return updated(environment, changeSet.toArray());
    }

    public boolean isArchived() {
        return status.equals(EnvironmentStatus.ARCHIVED);
    }

    public boolean isActive() {
        return status.equals(EnvironmentStatus.ACTIVE);
    }

}
