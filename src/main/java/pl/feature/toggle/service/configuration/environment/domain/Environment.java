package pl.feature.toggle.service.configuration.environment.domain;

import pl.feature.toggle.service.configuration.environment.application.port.in.command.CreateEnvironmentCommand;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentUpdateResult.EnvironmentFieldChange;
import pl.feature.toggle.service.configuration.environment.domain.exception.CannotOperateOnArchivedEnvironmentException;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pl.feature.toggle.service.configuration.environment.domain.EnvironmentUpdateResult.EnvironmentFieldChange.change;
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
        EnvironmentId environmentId = EnvironmentId.create();
        return new Environment(environmentId, command.projectId(), command.name(), command.type(), EnvironmentStatus.ACTIVE);
    }

    public EnvironmentUpdateResult archive() {
        if (isArchived()) {
            return noChanges();
        }
        var change = change(EnvironmentField.STATUS, status, EnvironmentStatus.ARCHIVED);
        return updated(new Environment(id, projectId, name, type, EnvironmentStatus.ARCHIVED), change);
    }

    public EnvironmentUpdateResult restore() {
        if (isActive()) {
            return noChanges();
        }
        var change = change(EnvironmentField.STATUS, status, EnvironmentStatus.ACTIVE);
        return updated(new Environment(id, projectId, name, type, EnvironmentStatus.ACTIVE), change);
    }

    public EnvironmentUpdateResult changeType(EnvironmentType newType) {
        if (isArchived()) {
            throw new CannotOperateOnArchivedEnvironmentException(id);
        }
        if (type.equals(newType)) {
            return noChanges();
        }
        var change = change(EnvironmentField.TYPE, type, newType);
        return updated(new Environment(id, projectId, name, newType, status), change);
    }

    public EnvironmentUpdateResult update(EnvironmentName environmentName) {
        if (isArchived()) {
            throw new CannotOperateOnArchivedEnvironmentException(id);
        }
        List<EnvironmentFieldChange> changes = new ArrayList<>();
        if (!Objects.equals(environmentName, name)) {
            changes.add(change(EnvironmentField.NAME, name, environmentName));
        }

        var environment = new Environment(id, projectId, environmentName, type, status);
        return EnvironmentUpdateResult.of(environment, changes);
    }

    public boolean isArchived() {
        return status.equals(EnvironmentStatus.ARCHIVED);
    }

    public boolean isActive() {
        return status.equals(EnvironmentStatus.ACTIVE);
    }

}
