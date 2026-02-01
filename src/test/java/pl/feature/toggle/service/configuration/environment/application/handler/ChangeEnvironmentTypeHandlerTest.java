package pl.feature.toggle.service.configuration.environment.application.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.feature.toggle.service.configuration.AbstractUnitTest;
import pl.feature.toggle.service.configuration.environment.application.port.in.ChangeEnvironmentTypeUseCase;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentType;
import pl.feature.toggle.service.configuration.environment.domain.exception.CannotOperateOnArchivedEnvironmentException;
import pl.feature.toggle.service.configuration.environment.domain.exception.CannotOperateOnEnvironmentForArchivedProjectException;
import pl.feature.toggle.service.configuration.environment.domain.exception.EnvironmentNotFoundException;
import pl.feature.toggle.service.configuration.project.domain.ProjectStatus;
import pl.feature.toggle.service.configuration.project.domain.exception.CannotOperateOnArchivedProjectException;
import pl.feature.toggle.service.contracts.event.environment.EnvironmentTypeChanged;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static pl.feature.toggle.service.configuration.builder.FakeChangeEnvironmentTypeCommandBuilder.fakeChangeEnvironmentTypeCommandBuilder;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.PROJECT_ENV;

class ChangeEnvironmentTypeHandlerTest extends AbstractUnitTest {

    private ChangeEnvironmentTypeUseCase sut;

    @BeforeEach
    void setUp() {
        sut = EnvironmentHandlerFacade.changeEnvironmentTypeUseCase(environmentCommandRepositorySpy, environmentQueryRepositoryStub,
                environmentPolicyFacade, outboxWriter, actorProvider, correlationProvider);
    }

    @Test
    void should_change_environment_type() {
        // given
        var command = fakeChangeEnvironmentTypeCommandBuilder()
                .withType(EnvironmentType.TEST)
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
        assertThat(updated.type()).isEqualTo(command.type());
        assertContainsEventOfType(PROJECT_ENV.topic(), EnvironmentTypeChanged.class);
    }

    @Test
    void should_throw_exception_when_environment_does_not_exist() {
        // given
        var command = fakeChangeEnvironmentTypeCommandBuilder()
                .withType(EnvironmentType.TEST)
                .build();
        environmentCommandRepositorySpy.expectNoCalls();
        environmentQueryRepositoryStub.getOrThrowThrows(new EnvironmentNotFoundException(command.environmentId(), command.projectId()));

        // when
        var exception = catchException(() -> sut.handle(command));

        // then
        assertThat(exception).isInstanceOf(EnvironmentNotFoundException.class);
        assertNoEventsHasBeenPublished();
    }

    @Test
    void should_throw_exception_when_environment_is_archived() {
        // given
        var command = fakeChangeEnvironmentTypeCommandBuilder()
                .withType(EnvironmentType.TEST)
                .build();
        environmentQueryRepositoryStub.getOrThrowReturns(ARCHIVED_ENVIRONMENT);
        projectQueryRepositoryStub.fetchStatusReturns(ProjectStatus.ACTIVE);
        environmentCommandRepositorySpy.expectNoCalls();

        // when
        var exception = catchException(() -> sut.handle(command));

        // then
        assertThat(exception).isInstanceOf(CannotOperateOnArchivedEnvironmentException.class);
        assertNoEventsHasBeenPublished();
    }

    @Test
    void should_throw_exception_when_project_is_archived() {
        // given
        var command = fakeChangeEnvironmentTypeCommandBuilder()
                .withType(EnvironmentType.TEST)
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
}
