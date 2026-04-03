package com.configly.structure.project.domain;

import com.configly.model.Revision;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.configly.structure.project.domain.ProjectUpdateResult.ProjectFieldChange.fieldChange;

public record ProjectUpdateResult(
        Project project,
        Revision expectedRevision,
        List<ProjectFieldChange> changes
) {

    public static ProjectUpdateResult updated(Project project, Revision expectedRevision, ProjectFieldChange... changes) {
        return new ProjectUpdateResult(project, expectedRevision, List.of(changes));
    }

    public static ProjectUpdateResult noChanges(Project project) {
        return new ProjectUpdateResult(project, project.revision(), List.of());
    }

    public boolean wasUpdated() {
        return !changes.isEmpty();
    }

    public record ProjectFieldChange(
            ProjectField field,
            Object before,
            Object after
    ) {

        static ProjectFieldChange fieldChange(ProjectField field, Object before, Object after) {
            return new ProjectFieldChange(field, before, after);
        }
    }

    static class ChangeSet {
        private final List<ProjectFieldChange> changes = new ArrayList<>();

        static ChangeSet createChangeSet() {
            return new ChangeSet();
        }

        void addIfChanged(ProjectField field, Object before, Object after) {
            if (!Objects.equals(before, after)) {
                changes.add(fieldChange(field, before, after));
            }
        }

        public boolean isEmpty() {
            return changes.isEmpty();
        }

        public ProjectFieldChange[] toArray() {
            return changes.toArray(ProjectFieldChange[]::new);
        }
    }
}
