package pl.feature.toggle.service.configuration.project.domain;

import org.junit.jupiter.api.Test;
import pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult.ProjectFieldChange;
import pl.feature.toggle.service.configuration.project.domain.exception.CannotOperateOnArchivedProjectException;
import pl.feature.toggle.service.model.Revision;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;
import pl.feature.toggle.service.model.project.ProjectStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProjectTest {

    @Test
    void should_create_project_as_active() {
        // given
        var name = ProjectName.create("My project");
        var description = ProjectDescription.create("desc");

        // when
        var project = Project.create(name, description);

        // then
        assertThat(project.status()).isEqualTo(ProjectStatus.ACTIVE);
        assertThat(project.id()).isNotNull();
        assertThat(project.revision()).isEqualTo(Revision.initialRevision());
        assertThat(project.name()).isEqualTo(name);
        assertThat(project.description()).isEqualTo(description);
    }

    @Test
    void should_archive_active_project() {
        // given
        var project = activeProject();

        // when
        var result = project.archive();

        // then
        assertThat(result.wasUpdated()).isTrue();
        assertThat(result.project().status()).isEqualTo(ProjectStatus.ARCHIVED);
        assertThat(result.expectedRevision()).isEqualTo(Revision.initialRevision());
        assertThat(result.project().revision()).isEqualTo(result.expectedRevision().next());

        assertThat(result.changes())
                .anySatisfy(change -> {
                    assertThat(change.field()).isEqualTo(ProjectField.STATUS);
                    assertThat(change.before()).isEqualTo(ProjectStatus.ACTIVE);
                    assertThat(change.after()).isEqualTo(ProjectStatus.ARCHIVED);
                });
    }

    @Test
    void should_not_archive_archived_project() {
        // given
        var project = archivedProject();

        // when
        var result = project.archive();

        // then
        assertThat(result.wasUpdated()).isFalse();
        assertThat(result.project()).isEqualTo(project);
        assertThat(result.changes()).isEmpty();
        assertThat(result.expectedRevision()).isEqualTo(Revision.initialRevision());
        assertThat(result.project().revision()).isEqualTo(result.expectedRevision());
    }

    @Test
    void should_restore_archived_project() {
        // given
        var project = archivedProject();

        // when
        var result = project.restore();

        // then
        assertThat(result.wasUpdated()).isTrue();
        assertThat(result.project().status()).isEqualTo(ProjectStatus.ACTIVE);
        assertThat(result.expectedRevision()).isEqualTo(Revision.initialRevision());
        assertThat(result.project().revision()).isEqualTo(result.expectedRevision().next());

        assertThat(result.changes())
                .anySatisfy(change -> {
                    assertThat(change.field()).isEqualTo(ProjectField.STATUS);
                    assertThat(change.before()).isEqualTo(ProjectStatus.ARCHIVED);
                    assertThat(change.after()).isEqualTo(ProjectStatus.ACTIVE);
                });
    }

    @Test
    void should_not_restore_active_project() {
        // given
        var project = activeProject();

        // when
        var result = project.restore();

        // then
        assertThat(result.wasUpdated()).isFalse();
        assertThat(result.project()).isEqualTo(project);
        assertThat(result.expectedRevision()).isEqualTo(Revision.initialRevision());
        assertThat(result.project().revision()).isEqualTo(result.expectedRevision());
        assertThat(result.changes()).isEmpty();
    }

    @Test
    void should_throw_when_updating_archived_project() {
        // given
        var project = archivedProject();

        // when / then
        assertThatThrownBy(() -> project.update(
                ProjectName.create("New name"),
                ProjectDescription.create("New desc")
        )).isInstanceOf(CannotOperateOnArchivedProjectException.class);
    }

    @Test
    void should_return_no_changes_when_updating_with_same_values() {
        // given
        var project = activeProject();

        // when
        var result = project.update(project.name(), project.description());

        // then
        assertThat(result.wasUpdated()).isFalse();
        assertThat(result.project()).isEqualTo(project);
        assertThat(result.expectedRevision()).isEqualTo(Revision.initialRevision());
        assertThat(result.project().revision()).isEqualTo(result.expectedRevision());
        assertThat(result.changes()).isEmpty();
    }

    @Test
    void should_update_only_name_when_description_same() {
        // given
        var project = activeProject();
        var newName = ProjectName.create("Changed name");

        // when
        var result = project.update(newName, project.description());

        // then
        assertThat(result.wasUpdated()).isTrue();
        assertThat(result.project().name()).isEqualTo(newName);
        assertThat(result.project().description()).isEqualTo(project.description());
        assertThat(result.project().status()).isEqualTo(ProjectStatus.ACTIVE);
        assertThat(result.expectedRevision()).isEqualTo(Revision.initialRevision());
        assertThat(result.project().revision()).isEqualTo(result.expectedRevision().next());

        assertThat(result.changes())
                .hasSize(1)
                .first()
                .satisfies(change -> {
                    assertThat(change.field()).isEqualTo(ProjectField.NAME);
                    assertThat(change.before()).isEqualTo(project.name());
                    assertThat(change.after()).isEqualTo(newName);
                });
    }

    @Test
    void should_update_only_description_when_name_same() {
        // given
        var project = activeProject();
        var newDescription = ProjectDescription.create("Changed desc");

        // when
        var result = project.update(project.name(), newDescription);

        // then
        assertThat(result.wasUpdated()).isTrue();
        assertThat(result.project().name()).isEqualTo(project.name());
        assertThat(result.project().description()).isEqualTo(newDescription);
        assertThat(result.expectedRevision()).isEqualTo(Revision.initialRevision());
        assertThat(result.project().revision()).isEqualTo(result.expectedRevision().next());

        assertThat(result.changes())
                .hasSize(1)
                .first()
                .satisfies(change -> {
                    assertThat(change.field()).isEqualTo(ProjectField.DESCRIPTION);
                    assertThat(change.before()).isEqualTo(project.description());
                    assertThat(change.after()).isEqualTo(newDescription);
                });
    }

    @Test
    void should_update_name_and_description_when_both_changed() {
        // given
        var project = activeProject();
        var newName = ProjectName.create("Changed name");
        var newDesc = ProjectDescription.create("Changed desc");

        // when
        var result = project.update(newName, newDesc);

        // then
        assertThat(result.wasUpdated()).isTrue();
        assertThat(result.project().name()).isEqualTo(newName);
        assertThat(result.project().description()).isEqualTo(newDesc);
        assertThat(result.expectedRevision()).isEqualTo(Revision.initialRevision());
        assertThat(result.project().revision()).isEqualTo(result.expectedRevision().next());

        assertThat(result.changes())
                .extracting(ProjectFieldChange::field)
                .containsExactlyInAnyOrder(ProjectField.NAME, ProjectField.DESCRIPTION);
    }


    private static Project activeProject() {
        return new Project(
                ProjectId.create(),
                ProjectName.create("Name"),
                ProjectDescription.create("Desc"),
                ProjectStatus.ACTIVE,
                Revision.initialRevision()
        );
    }

    private static Project archivedProject() {
        return new Project(
                ProjectId.create(),
                ProjectName.create("Name"),
                ProjectDescription.create("Desc"),
                ProjectStatus.ARCHIVED,
                Revision.initialRevision()
        );
    }
}


