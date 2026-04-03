package com.configly.structure.environment.domain;

import com.configly.model.Revision;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.configly.structure.environment.domain.EnvironmentUpdateResult.EnvironmentFieldChange.fieldChange;

public record EnvironmentUpdateResult(
        Environment environment,
        Revision expectedRevision,
        List<EnvironmentFieldChange> changes
) {

    public static EnvironmentUpdateResult updated(Environment environment, Revision expectedRevision, EnvironmentFieldChange... changes) {
        return new EnvironmentUpdateResult(environment, expectedRevision, List.of(changes));
    }

    public static EnvironmentUpdateResult noChanges(Environment environment) {
        return new EnvironmentUpdateResult(environment, environment.revision(), List.of());
    }

    public static EnvironmentUpdateResult of(Environment environment, Revision expectedRevision, List<EnvironmentFieldChange> changes) {
        return new EnvironmentUpdateResult(environment, expectedRevision, changes);
    }

    public boolean wasUpdated() {
        return !changes.isEmpty();
    }

    public record EnvironmentFieldChange(
            EnvironmentField field,
            Object before,
            Object after
    ) {

        static EnvironmentFieldChange fieldChange(EnvironmentField field, Object before, Object after) {
            return new EnvironmentFieldChange(field, before, after);
        }

    }

    static class ChangeSet {
        private final List<EnvironmentFieldChange> changes = new ArrayList<>();

        static ChangeSet createChangeSet() {
            return new ChangeSet();
        }

        void addIfChanged(EnvironmentField field, Object before, Object after) {
            if (!Objects.equals(before, after)) {
                changes.add(fieldChange(field, before, after));
            }
        }

        public boolean isEmpty() {
            return changes.isEmpty();
        }

        public EnvironmentFieldChange[] toArray() {
            return changes.toArray(EnvironmentFieldChange[]::new);
        }
    }
}