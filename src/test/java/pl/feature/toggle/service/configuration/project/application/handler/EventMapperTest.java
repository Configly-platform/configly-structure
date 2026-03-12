package pl.feature.toggle.service.configuration.project.application.handler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.feature.toggle.service.configuration.AbstractUnitTest;
import pl.feature.toggle.service.configuration.project.domain.ProjectField;
import pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult;
import pl.feature.toggle.service.model.Revision;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;
import pl.feature.toggle.service.model.project.ProjectStatus;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.feature.toggle.service.configuration.builder.FakeProjectBuilder.fakeProjectBuilder;

class EventMapperTest extends AbstractUnitTest {

    @Test
    void should_map_project_to_project_created_event() {
        // given
        var project = fakeProjectBuilder().build();

        // when
        var event = EventMapper.createProjectCreatedEvent(project, actorProvider.current(), correlationProvider.current());

        // then
        assertThat(event.projectId()).isEqualTo(project.id().uuid());
        assertThat(event.projectName()).isEqualTo(project.name().value());
        assertThat(event.status()).isEqualTo(project.status().name());
        assertThat(event.revision()).isEqualTo(Revision.initialRevision().value());
        assertThat(event.eventId()).isNotNull();
        assertThat(event.metadata()).isEqualTo(metadata(event.metadata().occurredAt()));
    }

    @Test
    void should_map_to_project_updated_event() {
        // given
        var actor = actorProvider.current();
        var correlationId = correlationProvider.current();
        var project = fakeProjectBuilder().build();
        var updateResult = new ProjectUpdateResult(
                project,
                Revision.initialRevision(),
                List.of(new ProjectUpdateResult.ProjectFieldChange(
                        ProjectField.DESCRIPTION,
                        project.description(),
                        ProjectDescription.create("TEST")
                ))
        );

        // when
        var result = EventMapper.createProjectUpdatedEvent(updateResult, actor, correlationId);

        // then
        assertThat(result.projectId()).isEqualTo(project.id().uuid());
        assertThat(result.projectName()).isEqualTo(project.name().value());
        assertThat(result.projectDescription()).isEqualTo(project.description().value());
        assertThat(result.revision()).isEqualTo(Revision.initialRevision().value());
        assertThat(result.eventId()).isNotNull();
        assertThat(result.metadata().actorId()).isEqualTo(actor.idAsString());
        assertThat(result.metadata().username()).isEqualTo(actor.usernameAsString());
        assertThat(result.metadata().correlationId()).isEqualTo(correlationId.value());

        assertThat(result.changes().changes()).hasSize(1);
        var change = result.changes().changes().getFirst();
        assertThat(change.field()).isEqualTo(ProjectField.DESCRIPTION.name());
        assertThat(change.before()).isEqualTo(project.description().value());
        assertThat(change.after()).isEqualTo("TEST");
    }

    @Test
    void should_map_project_status_changed_event() {
        // given
        var actor = actorProvider.current();
        var correlationId = correlationProvider.current();

        var projectId = ProjectId.create();
        var updatedProject = fakeProjectBuilder()
                .withId(projectId)
                .withStatus(ProjectStatus.ARCHIVED)
                .build();

        var updateResult = new ProjectUpdateResult(
                updatedProject,
                Revision.initialRevision(),
                List.of(new ProjectUpdateResult.ProjectFieldChange(
                        ProjectField.STATUS,
                        ProjectStatus.ACTIVE,
                        ProjectStatus.ARCHIVED
                ))
        );

        // when
        var event = EventMapper.createProjectStatusChangedEvent(updateResult, actor, correlationId);

        // then
        assertThat(event.projectId()).isEqualTo(projectId.uuid());
        assertThat(event.status()).isEqualTo(ProjectStatus.ARCHIVED.name());
        assertThat(event.revision()).isEqualTo(Revision.initialRevision().value());

        assertThat(event.metadata().actorId()).isEqualTo(actor.idAsString());
        assertThat(event.metadata().username()).isEqualTo(actor.usernameAsString());
        assertThat(event.metadata().correlationId()).isEqualTo(correlationId.value());

        assertThat(event.changes().changes()).hasSize(1);
        var change = event.changes().changes().getFirst();
        assertThat(change.field()).isEqualTo(ProjectField.STATUS.name());
        assertThat(change.before()).isEqualTo(ProjectStatus.ACTIVE.name());
        assertThat(change.after()).isEqualTo(ProjectStatus.ARCHIVED.name());
    }

    @ParameterizedTest(name = "{0} should be serialized correctly in changes")
    @MethodSource("serializeCases")
    void should_serialize_all_project_fields_in_changes(
            ProjectField field,
            Object before,
            Object after,
            String expectedBefore,
            String expectedAfter
    ) {
        // given
        var project = fakeProjectBuilder()
                .withName(ProjectName.create("TEST"))
                .withDescription(ProjectDescription.create("DESC"))
                .withStatus(ProjectStatus.ACTIVE)
                .build();

        var updateResult = new ProjectUpdateResult(
                project,
                Revision.initialRevision(),
                List.of(new ProjectUpdateResult.ProjectFieldChange(field, before, after))
        );

        // when
        var event = EventMapper.createProjectUpdatedEvent(
                updateResult,
                actorProvider.current(),
                correlationProvider.current()
        );

        // then
        assertThat(event.changes().changes()).hasSize(1);

        var change = event.changes().changes().getFirst();
        assertThat(change.field()).isEqualTo(field.name());
        assertThat(change.before()).isEqualTo(expectedBefore);
        assertThat(change.after()).isEqualTo(expectedAfter);
    }

    static Stream<Arguments> serializeCases() {
        var nameBefore = ProjectName.create("OLD_NAME");
        var nameAfter = ProjectName.create("NEW_NAME");

        var descBefore = ProjectDescription.create("OLD_DESC");
        var descAfter = ProjectDescription.create("NEW_DESC");

        var statusBefore = ProjectStatus.ACTIVE;
        var statusAfter = ProjectStatus.ARCHIVED;

        return Stream.of(
                Arguments.of(
                        ProjectField.NAME,
                        nameBefore, nameAfter,
                        nameBefore.value(), nameAfter.value()
                ),
                Arguments.of(
                        ProjectField.DESCRIPTION,
                        descBefore, descAfter,
                        descBefore.value(), descAfter.value()
                ),
                Arguments.of(
                        ProjectField.STATUS,
                        statusBefore, statusAfter,
                        statusBefore.name(), statusAfter.name()
                ),
                Arguments.of(
                        ProjectField.NAME,
                        null, ProjectName.create("X"),
                        null, "X"
                )
        );
    }


}