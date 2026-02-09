package pl.feature.toggle.service.configuration.environment.application.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.feature.toggle.service.configuration.AbstractUnitTest;
import pl.feature.toggle.service.configuration.environment.application.port.in.ChangeEnvironmentStatusUseCase;
import pl.feature.toggle.service.configuration.environment.domain.exception.CannotOperateOnEnvironmentForArchivedProjectException;
import pl.feature.toggle.service.configuration.environment.domain.exception.EnvironmentNotFoundException;
import pl.feature.toggle.service.contracts.event.environment.EnvironmentStatusChanged;
import pl.feature.toggle.service.model.environment.EnvironmentStatus;
import pl.feature.toggle.service.model.project.ProjectStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static pl.feature.toggle.service.configuration.builder.FakeChangeEnvironmentStatusCommandBuilder.fakeChangeEnvironmentStatusCommandBuilder;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.PROJECT_ENV;

class ChangeEnvironmentStatusHandlerTest extends AbstractUnitTest {

    private ChangeEnvironmentStatusUseCase sut;

    @BeforeEach
    void setUp() {
        sut = EnvironmentHandlerFacade.changeEnvironmentStatusUseCase(environmentCommandRepositorySpy, environmentQueryRepositoryStub,
                environmentPolicyFacade, outboxWriter, actorProvider, correlationProvider);
    }

    @Test
    void should_change_environment_status_to_archived() {
        // given
        var command = fakeChangeEnvironmentStatusCommandBuilder()
                .withNewEnvironmentStatus(EnvironmentStatus.ARCHIVED)
                .build();
        environmentQueryRepositoryStub.getOrThrowReturns(ACTIVE_ENVIRONMENT);
        projectQueryRepositoryStub.fetchStatusReturns(ProjectStatus.ACTIVE);

        // when
        sut.handle(command);

        // then
        var updated = environmentCommandRepositorySpy.getUpdated();
        var saved = environmentCommandRepositorySpy.getSaved();
        assertThat(saved).isNull();
        assertThat(updated).isNotNull();
        assertThat(updated.status()).isEqualTo(command.newEnvironmentStatus());
        assertContainsEventOfType(PROJECT_ENV.topic(), EnvironmentStatusChanged.class);
    }

    @Test
    void should_change_environment_status_to_active() {
        // given
        var command = fakeChangeEnvironmentStatusCommandBuilder()
                .withNewEnvironmentStatus(EnvironmentStatus.ACTIVE)
                .build();
        environmentQueryRepositoryStub.getOrThrowReturns(ARCHIVED_ENVIRONMENT);
        projectQueryRepositoryStub.fetchStatusReturns(ProjectStatus.ACTIVE);

        // when
        sut.handle(command);

        // then
        var updated = environmentCommandRepositorySpy.getUpdated();
        var saved = environmentCommandRepositorySpy.getSaved();
        assertThat(saved).isNull();
        assertThat(updated).isNotNull();
        assertThat(updated.status()).isEqualTo(command.newEnvironmentStatus());
        assertContainsEventOfType(PROJECT_ENV.topic(), EnvironmentStatusChanged.class);
    }

    @Test
    void should_throw_exception_when_environment_not_exists() {
        // given
        var command = fakeChangeEnvironmentStatusCommandBuilder()
                .withNewEnvironmentStatus(EnvironmentStatus.ARCHIVED)
                .build();
        environmentQueryRepositoryStub.getOrThrowThrows(new EnvironmentNotFoundException(command.environmentId(), command.projectId()));
        environmentCommandRepositorySpy.expectNoCalls();

        // when
        var exception = catchException(() -> sut.handle(command));

        // then
        assertThat(exception).isInstanceOf(EnvironmentNotFoundException.class);
        assertNoEventsHasBeenPublished();
    }

    @Test
    void should_throw_exception_when_project_is_archived() {
        // given
        var command = fakeChangeEnvironmentStatusCommandBuilder()
                .withNewEnvironmentStatus(EnvironmentStatus.ARCHIVED)
                .build();
        environmentQueryRepositoryStub.getOrThrowReturns(ACTIVE_ENVIRONMENT);
        projectQueryRepositoryStub.fetchStatusReturns(ProjectStatus.ARCHIVED);
        environmentCommandRepositorySpy.expectNoCalls();

        // when
        var exception = catchException(() -> sut.handle(command));

        // then
        assertThat(exception).isInstanceOf(CannotOperateOnEnvironmentForArchivedProjectException.class);
        assertNoEventsHasBeenPublished();
    }

    @Test
    void should_do_nothing_when_archive_environment_that_is_already_archived() {
        // given
        var command = fakeChangeEnvironmentStatusCommandBuilder()
                .withNewEnvironmentStatus(EnvironmentStatus.ARCHIVED)
                .build();
        environmentQueryRepositoryStub.getOrThrowReturns(ARCHIVED_ENVIRONMENT);
        projectQueryRepositoryStub.fetchStatusReturns(ProjectStatus.ACTIVE);
        environmentCommandRepositorySpy.expectNoCalls();

        // when
        sut.handle(command);

        // then
        assertNoEventsHasBeenPublished();
    }

    @Test
    void should_do_nothing_when_activate_environment_that_is_already_active() {
        // given
        var command = fakeChangeEnvironmentStatusCommandBuilder()
                .withNewEnvironmentStatus(EnvironmentStatus.ACTIVE)
                .build();
        environmentQueryRepositoryStub.getOrThrowReturns(ACTIVE_ENVIRONMENT);
        projectQueryRepositoryStub.fetchStatusReturns(ProjectStatus.ACTIVE);
        environmentCommandRepositorySpy.expectNoCalls();

        // when
        sut.handle(command);

        // then
        assertNoEventsHasBeenPublished();
    }
}
