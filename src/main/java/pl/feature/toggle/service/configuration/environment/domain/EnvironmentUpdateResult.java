package pl.feature.toggle.service.configuration.environment.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pl.feature.toggle.service.configuration.environment.domain.EnvironmentUpdateResult.EnvironmentFieldChange.fieldChange;

public record EnvironmentUpdateResult(
        Environment environment,
        List<EnvironmentFieldChange> changes
) {

    public static EnvironmentUpdateResult updated(Environment environment, EnvironmentFieldChange... changes) {
        return new EnvironmentUpdateResult(environment, List.of(changes));
    }

    public static EnvironmentUpdateResult noChanges(Environment environment) {
        return new EnvironmentUpdateResult(environment, List.of());
    }

    public static EnvironmentUpdateResult of(Environment environment, List<EnvironmentFieldChange> changes) {
        return new EnvironmentUpdateResult(environment, changes);
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