package pl.feature.toggle.service.configuration.project.domain;

import java.util.List;

public record ProjectUpdateResult(
        Project project,
        List<ProjectFieldChange> changes
) {

    public static ProjectUpdateResult updated(Project project, ProjectFieldChange... changes) {
        return new ProjectUpdateResult(project, List.of(changes));
    }

    public static ProjectUpdateResult noChanges() {
        return new ProjectUpdateResult(null, List.of());
    }

    public static ProjectUpdateResult of(Project project, List<ProjectFieldChange> changes) {
        return new ProjectUpdateResult(project, changes);
    }

    public boolean wasUpdated() {
        return !changes.isEmpty();
    }

    public record ProjectFieldChange(
            ProjectField field,
            Object before,
            Object after
    ) {

        static ProjectFieldChange change(ProjectField field, Object before, Object after) {
            return new ProjectFieldChange(field, before, after);
        }

    }
}
