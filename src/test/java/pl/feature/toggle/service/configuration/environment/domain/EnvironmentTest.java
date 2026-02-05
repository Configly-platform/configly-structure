package pl.feature.toggle.service.configuration.environment.domain;

import org.junit.jupiter.api.Test;
import pl.feature.toggle.service.configuration.environment.application.port.in.command.CreateEnvironmentCommand;
import pl.feature.toggle.service.configuration.environment.domain.exception.CannotOperateOnArchivedEnvironmentException;
import pl.feature.toggle.service.model.Revision;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnvironmentTest {

    @Test
    void should_create_environment_as_active_from_command() {
        // given
        var command = new CreateEnvironmentCommand(
                EnvironmentName.create("DEV"),
                ProjectId.create(),
                EnvironmentType.DEV
        );

        // when
        var env = Environment.create(command);

        // then
        assertThat(env.id()).isNotNull();
        assertThat(env.projectId()).isEqualTo(command.projectId());
        assertThat(env.name()).isEqualTo(command.name());
        assertThat(env.revision()).isEqualTo(Revision.initialRevision());
        assertThat(env.type()).isEqualTo(command.type());
        assertThat(env.status()).isEqualTo(EnvironmentStatus.ACTIVE);
    }

    @Test
    void should_create_environment_as_active_from_factory() {
        // given
        var projectId = ProjectId.create();
        var name = EnvironmentName.create("PROD");
        var type = EnvironmentType.PROD;

        // when
        var env = Environment.create(projectId, name, type);

        // then
        assertThat(env.id()).isNotNull();
        assertThat(env.revision()).isEqualTo(Revision.initialRevision());
        assertThat(env.projectId()).isEqualTo(projectId);
        assertThat(env.name()).isEqualTo(name);
        assertThat(env.type()).isEqualTo(type);
        assertThat(env.status()).isEqualTo(EnvironmentStatus.ACTIVE);
    }

    @Test
    void should_archive_active_environment() {
        // given
        var env = activeEnvironment();

        // when
        var result = env.archive();

        // then
        assertThat(result.wasUpdated()).isTrue();
        assertThat(result.expectedRevision()).isEqualTo(Revision.initialRevision());
        assertThat(result.environment().revision()).isEqualTo(env.revision().next());
        assertThat(result.environment().status()).isEqualTo(EnvironmentStatus.ARCHIVED);

        assertThat(result.changes())
                .anySatisfy(change -> {
                    assertThat(change.field()).isEqualTo(EnvironmentField.STATUS);
                    assertThat(change.before()).isEqualTo(EnvironmentStatus.ACTIVE);
                    assertThat(change.after()).isEqualTo(EnvironmentStatus.ARCHIVED);
                });
    }

    @Test
    void should_not_archive_archived_environment() {
        // given
        var env = archivedEnvironment();

        // when
        var result = env.archive();

        // then
        assertThat(result.wasUpdated()).isFalse();
        assertThat(result.environment()).isEqualTo(env);
        assertThat(result.changes()).isEmpty();
        assertThat(result.expectedRevision()).isEqualTo(Revision.initialRevision());
        assertThat(result.environment().revision()).isEqualTo(env.revision());
    }

    @Test
    void should_restore_archived_environment() {
        // given
        var env = archivedEnvironment();

        // when
        var result = env.restore();

        // then
        assertThat(result.wasUpdated()).isTrue();
        assertThat(result.environment().status()).isEqualTo(EnvironmentStatus.ACTIVE);
        assertThat(result.expectedRevision()).isEqualTo(Revision.initialRevision());
        assertThat(result.environment().revision()).isEqualTo(env.revision().next());

        assertThat(result.changes())
                .anySatisfy(change -> {
                    assertThat(change.field()).isEqualTo(EnvironmentField.STATUS);
                    assertThat(change.before()).isEqualTo(EnvironmentStatus.ARCHIVED);
                    assertThat(change.after()).isEqualTo(EnvironmentStatus.ACTIVE);
                });
    }

    @Test
    void should_not_restore_active_environment() {
        // given
        var env = activeEnvironment();

        // when
        var result = env.restore();

        // then
        assertThat(result.wasUpdated()).isFalse();
        assertThat(result.environment()).isEqualTo(env);
        assertThat(result.changes()).isEmpty();
        assertThat(result.expectedRevision()).isEqualTo(Revision.initialRevision());
        assertThat(result.environment().revision()).isEqualTo(env.revision());
    }

    @Test
    void should_throw_when_updating_archived_environment_name() {
        // given
        var env = archivedEnvironment();

        // when / then
        assertThatThrownBy(() -> env.update(EnvironmentName.create("NEW")))
                .isInstanceOf(CannotOperateOnArchivedEnvironmentException.class);
    }

    @Test
    void should_throw_when_changing_type_on_archived_environment() {
        // given
        var env = archivedEnvironment();

        // when / then
        assertThatThrownBy(() -> env.changeType(EnvironmentType.PROD))
                .isInstanceOf(CannotOperateOnArchivedEnvironmentException.class);
    }

    @Test
    void should_return_no_changes_when_updating_with_same_name() {
        // given
        var env = activeEnvironment();

        // when
        var result = env.update(env.name());

        // then
        assertThat(result.wasUpdated()).isFalse();
        assertThat(result.environment()).isEqualTo(env);
        assertThat(result.changes()).isEmpty();
        assertThat(result.expectedRevision()).isEqualTo(Revision.initialRevision());
        assertThat(result.environment().revision()).isEqualTo(env.revision());
    }

    @Test
    void should_update_name_when_changed() {
        // given
        var env = activeEnvironment();
        var newName = EnvironmentName.create("NEW-NAME");

        // when
        var result = env.update(newName);

        // then
        assertThat(result.wasUpdated()).isTrue();
        assertThat(result.environment().name()).isEqualTo(newName);
        assertThat(result.environment().type()).isEqualTo(env.type());
        assertThat(result.environment().status()).isEqualTo(EnvironmentStatus.ACTIVE);
        assertThat(result.environment().projectId()).isEqualTo(env.projectId());
        assertThat(result.expectedRevision()).isEqualTo(Revision.initialRevision());
        assertThat(result.environment().revision()).isEqualTo(env.revision().next());

        assertThat(result.changes())
                .hasSize(1)
                .first()
                .satisfies(change -> {
                    assertThat(change.field()).isEqualTo(EnvironmentField.NAME);
                    assertThat(change.before()).isEqualTo(env.name());
                    assertThat(change.after()).isEqualTo(newName);
                });
    }

    @Test
    void should_return_no_changes_when_changing_type_to_same_value() {
        // given
        var env = activeEnvironment();

        // when
        var result = env.changeType(env.type());

        // then
        assertThat(result.wasUpdated()).isFalse();
        assertThat(result.environment()).isEqualTo(env);
        assertThat(result.changes()).isEmpty();
        assertThat(result.expectedRevision()).isEqualTo(Revision.initialRevision());
        assertThat(result.environment().revision()).isEqualTo(env.revision());
    }

    @Test
    void should_change_type_when_changed() {
        // given
        var env = activeEnvironment();
        var newType = EnvironmentType.PROD;

        // when
        var result = env.changeType(newType);

        // then
        assertThat(result.wasUpdated()).isTrue();
        assertThat(result.expectedRevision()).isEqualTo(Revision.initialRevision());
        assertThat(result.environment().revision()).isEqualTo(env.revision().next());
        assertThat(result.environment().type()).isEqualTo(newType);
        assertThat(result.environment().name()).isEqualTo(env.name());
        assertThat(result.environment().status()).isEqualTo(EnvironmentStatus.ACTIVE);

        assertThat(result.changes())
                .hasSize(1)
                .first()
                .satisfies(change -> {
                    assertThat(change.field()).isEqualTo(EnvironmentField.TYPE);
                    assertThat(change.before()).isEqualTo(env.type());
                    assertThat(change.after()).isEqualTo(newType);
                });
    }

    private static Environment activeEnvironment() {
        return new Environment(
                EnvironmentId.create(),
                ProjectId.create(),
                EnvironmentName.create("name"),
                EnvironmentType.DEV,
                EnvironmentStatus.ACTIVE,
                Revision.initialRevision()
        );
    }

    private static Environment archivedEnvironment() {
        return new Environment(
                EnvironmentId.create(),
                ProjectId.create(),
                EnvironmentName.create("name"),
                EnvironmentType.DEV,
                EnvironmentStatus.ARCHIVED,
                Revision.initialRevision()
        );
    }
}
