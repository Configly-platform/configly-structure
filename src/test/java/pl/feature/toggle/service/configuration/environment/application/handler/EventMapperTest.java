package pl.feature.toggle.service.configuration.environment.application.handler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.feature.toggle.service.configuration.AbstractUnitTest;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentField;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentStatus;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentType;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentUpdateResult;
import pl.feature.toggle.service.model.Revision;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.feature.toggle.service.configuration.builder.FakeEnvironmentBuilder.fakeEnvironmentBuilder;

class EventMapperTest extends AbstractUnitTest {

    @Test
    void should_map_environment_to_environment_created_event() {
        // given
        var projectId = UUID.randomUUID().toString();
        var environment = createEnvironment(projectId, "TEST");

        // when
        var event = EventMapper.createEnvironmentCreatedEvent(environment, actorProvider.current(), correlationProvider.current());

        // then
        assertThat(event.environmentId()).isEqualTo(environment.id().uuid());
        assertThat(event.projectId()).isEqualTo(UUID.fromString(projectId));
        assertThat(event.environmentName()).isEqualTo(environment.name().value());
        assertThat(event.eventId()).isNotNull();
        assertThat(event.revision()).isEqualTo(Revision.initialRevision().value());
        assertThat(event.metadata()).isEqualTo(metadata(event.metadata().occurredAt()));
    }

    @Test
    void should_map_environment_updated_event() {
        // given
        var projectId = ProjectId.create();
        var environment = fakeEnvironmentBuilder()
                .withProjectId(projectId)
                .withName("NEW_NAME")
                .withStatus(EnvironmentStatus.ACTIVE)
                .withType(EnvironmentType.TEST)
                .build();

        var updateResult = new EnvironmentUpdateResult(
                environment,
                Revision.initialRevision(),
                List.of(new EnvironmentUpdateResult.EnvironmentFieldChange(
                        EnvironmentField.NAME,
                        EnvironmentName.create("OLD_NAME"),
                        EnvironmentName.create("NEW_NAME")
                ))
        );

        // when
        var event = EventMapper.createEnvironmentUpdatedEvent(
                updateResult,
                actorProvider.current(),
                correlationProvider.current()
        );

        // then
        assertThat(event.environmentId()).isEqualTo(environment.id().uuid());
        assertThat(event.projectId()).isEqualTo(environment.projectId().uuid());
        assertThat(event.environmentName()).isEqualTo(environment.name().value());
        assertThat(event.eventId()).isNotNull();
        assertThat(event.revision()).isEqualTo(Revision.initialRevision().value());

        assertThat(event.metadata()).isEqualTo(metadata(event.metadata().occurredAt()));

        assertThat(event.changes().changes()).hasSize(1);
        var change = event.changes().changes().getFirst();
        assertThat(change.field()).isEqualTo(EnvironmentField.NAME.name());
        assertThat(change.before()).isEqualTo("OLD_NAME");
        assertThat(change.after()).isEqualTo("NEW_NAME");
    }

    @Test
    void should_map_environment_status_changed_event() {
        // given
        var projectId = ProjectId.create();
        var environment = fakeEnvironmentBuilder()
                .withProjectId(projectId)
                .withName("ENV")
                .withStatus(EnvironmentStatus.ARCHIVED)
                .withType(EnvironmentType.TEST)
                .build();

        var updateResult = new EnvironmentUpdateResult(
                environment,
                Revision.initialRevision(),
                List.of(new EnvironmentUpdateResult.EnvironmentFieldChange(
                        EnvironmentField.STATUS,
                        EnvironmentStatus.ACTIVE,
                        EnvironmentStatus.ARCHIVED
                ))
        );

        // when
        var event = EventMapper.createEnvironmentStatusChangedEvent(
                updateResult,
                actorProvider.current(),
                correlationProvider.current()
        );

        // then
        assertThat(event.environmentId()).isEqualTo(environment.id().uuid());
        assertThat(event.projectId()).isEqualTo(environment.projectId().uuid());
        assertThat(event.status()).isEqualTo(environment.status().name());
        assertThat(event.eventId()).isNotNull();
        assertThat(event.revision()).isEqualTo(Revision.initialRevision().value());

        assertThat(event.metadata()).isEqualTo(metadata(event.metadata().occurredAt()));

        assertThat(event.changes().changes()).hasSize(1);
        var change = event.changes().changes().getFirst();
        assertThat(change.field()).isEqualTo(EnvironmentField.STATUS.name());
        assertThat(change.before()).isEqualTo(EnvironmentStatus.ACTIVE.name());
        assertThat(change.after()).isEqualTo(EnvironmentStatus.ARCHIVED.name());
    }

    @Test
    void should_map_environment_type_changed_event() {
        // given
        var projectId = ProjectId.create();
        var environment = fakeEnvironmentBuilder()
                .withProjectId(projectId)
                .withName("ENV")
                .withStatus(EnvironmentStatus.ACTIVE)
                .withType(EnvironmentType.PROD)
                .build();

        var updateResult = new EnvironmentUpdateResult(
                environment,
                Revision.initialRevision(),
                List.of(new EnvironmentUpdateResult.EnvironmentFieldChange(
                        EnvironmentField.TYPE,
                        EnvironmentType.TEST,
                        EnvironmentType.PROD
                ))
        );

        // when
        var event = EventMapper.createEnvironmentTypeChangedEvent(
                updateResult,
                actorProvider.current(),
                correlationProvider.current()
        );

        // then
        assertThat(event.environmentId()).isEqualTo(environment.id().uuid());
        assertThat(event.projectId()).isEqualTo(environment.projectId().uuid());
        assertThat(event.type()).isEqualTo(environment.type().name());
        assertThat(event.eventId()).isNotNull();
        assertThat(event.revision()).isEqualTo(Revision.initialRevision().value());

        assertThat(event.metadata()).isEqualTo(metadata(event.metadata().occurredAt()));

        assertThat(event.changes().changes()).hasSize(1);
        var change = event.changes().changes().getFirst();
        assertThat(change.field()).isEqualTo(EnvironmentField.TYPE.name());
        assertThat(change.before()).isEqualTo(EnvironmentType.TEST.name());
        assertThat(change.after()).isEqualTo(EnvironmentType.PROD.name());
    }


    @ParameterizedTest(name = "{0} should be serialized correctly in changes")
    @MethodSource("serializeCases")
    void should_serialize_all_environment_fields_in_changes(
            EnvironmentField field,
            Object before,
            Object after,
            String expectedBefore,
            String expectedAfter
    ) {
        // given
        var projectId = ProjectId.create();
        var environment = fakeEnvironmentBuilder()
                .withProjectId(projectId)
                .withName("ENV")
                .withStatus(EnvironmentStatus.ACTIVE)
                .withType(EnvironmentType.TEST)
                .build();

        var updateResult = new EnvironmentUpdateResult(
                environment,
                Revision.initialRevision(),
                List.of(new EnvironmentUpdateResult.EnvironmentFieldChange(field, before, after))
        );

        // when
        var event = EventMapper.createEnvironmentStatusChangedEvent(
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
        var nameBefore = EnvironmentName.create("OLD_NAME");
        var nameAfter = EnvironmentName.create("NEW_NAME");

        var projectIdBefore = ProjectId.create();
        var projectIdAfter = ProjectId.create();

        var statusBefore = EnvironmentStatus.ACTIVE;
        var statusAfter = EnvironmentStatus.ARCHIVED;

        var typeBefore = EnvironmentType.TEST;
        var typeAfter = EnvironmentType.PROD;

        return Stream.of(
                Arguments.of(
                        EnvironmentField.NAME,
                        nameBefore, nameAfter,
                        nameBefore.value(), nameAfter.value()
                ),
                Arguments.of(
                        EnvironmentField.PROJECT_ID,
                        projectIdBefore, projectIdAfter,
                        projectIdBefore.idAsString(), projectIdAfter.idAsString()
                ),
                Arguments.of(
                        EnvironmentField.STATUS,
                        statusBefore, statusAfter,
                        statusBefore.name(), statusAfter.name()
                ),
                Arguments.of(
                        EnvironmentField.TYPE,
                        typeBefore, typeAfter,
                        typeBefore.name(), typeAfter.name()
                ),
                Arguments.of(
                        EnvironmentField.NAME,
                        null, EnvironmentName.create("X"),
                        null, "X"
                )
        );
    }
}
