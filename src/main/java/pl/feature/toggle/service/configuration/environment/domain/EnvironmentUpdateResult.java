package pl.feature.toggle.service.configuration.environment.domain;

import java.util.List;

public record EnvironmentUpdateResult(
        Environment environment,
        List<EnvironmentFieldChange> changes
) {

    public static EnvironmentUpdateResult updated(Environment environment, EnvironmentFieldChange... changes) {
        return new EnvironmentUpdateResult(environment, List.of(changes));
    }

    public static EnvironmentUpdateResult noChanges() {
        return new EnvironmentUpdateResult(null, List.of());
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

        static EnvironmentFieldChange change(EnvironmentField field, Object before, Object after) {
            return new EnvironmentFieldChange(field, before, after);
        }

    }
}