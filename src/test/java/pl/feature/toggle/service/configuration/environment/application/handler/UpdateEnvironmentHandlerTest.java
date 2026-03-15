package pl.feature.toggle.service.configuration.environment.application.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.feature.toggle.service.configuration.AbstractUnitTest;
import pl.feature.toggle.service.configuration.environment.application.port.in.UpdateEnvironmentUseCase;
import pl.feature.toggle.service.configuration.environment.domain.exception.CannotOperateOnArchivedEnvironmentException;
import pl.feature.toggle.service.configuration.environment.domain.exception.CannotOperateOnEnvironmentForArchivedProjectException;
import pl.feature.toggle.service.configuration.environment.domain.exception.EnvironmentAlreadyExistsException;
import pl.feature.toggle.service.configuration.environment.domain.exception.EnvironmentNotFoundException;
import pl.feature.toggle.service.contracts.event.environment.EnvironmentUpdated;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static pl.feature.toggle.service.configuration.builder.FakeUpdateEnvironmentCommandBuilder.fakeUpdateEnvironmentCommandBuilder;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.CONFIGURATION;

class UpdateEnvironmentHandlerTest extends AbstractUnitTest {

    private UpdateEnvironmentUseCase sut;

    @BeforeEach
    void setUp() {
        sut = EnvironmentHandlerFacade.updateEnvironmentUseCase(environmentCommandRepositorySpy, environmentQueryRepositoryStub,
                environmentPolicyFacade, outboxWriter);
    }

    @Test
    void should_update_environment() {
        // given
        var command = fakeUpdateEnvironmentCommandBuilder()
                .withName(EnvironmentName.create("UpdatedName"))
                .build();
        environmentQueryRepositoryStub.getOrThrowReturns(ACTIVE_ENVIRONMENT);
        environmentQueryRepositoryStub.existsByProjectIdAndNameReturns(false);
        projectQueryRepositoryStub.fetchStatusReturns(ProjectStatus.ACTIVE);

        // when
        sut.handle(command);

        // then
        var updated = environmentCommandRepositorySpy.getUpdated();
        var saved = environmentCommandRepositorySpy.getSaved();
        assertThat(saved).isNull();
        assertThat(updated).isNotNull();
        assertThat(updated.name()).isEqualTo(command.name());
        assertContainsEventOfType(CONFIGURATION.topic(), EnvironmentUpdated.class);
    }

    @Test
    void should_throw_exception_when_update_environment_that_does_not_exist() {
        // given
        var command = fakeUpdateEnvironmentCommandBuilder()
                .build();
        environmentQueryRepositoryStub.getOrThrowThrows(new EnvironmentNotFoundException(command.environmentId(), command.projectId()));
        projectQueryRepositoryStub.fetchStatusReturns(ProjectStatus.ACTIVE);
        environmentCommandRepositorySpy.expectNoCalls();

        // when
        var exception = catchException(() -> sut.handle(command));

        // then
        assertThat(exception).isInstanceOf(EnvironmentNotFoundException.class);
        assertNoEventsHasBeenPublished();
    }

    @Test
    void should_throw_exception_when_environment_already_exist() {
        // given
        var command = fakeUpdateEnvironmentCommandBuilder()
                .build();
        environmentQueryRepositoryStub.getOrThrowReturns(ACTIVE_ENVIRONMENT);
        environmentQueryRepositoryStub.existsByProjectIdAndNameReturns(true);
        environmentCommandRepositorySpy.expectNoCalls();
        projectQueryRepositoryStub.fetchStatusReturns(ProjectStatus.ACTIVE);

        // when
        var exception = catchException(() -> sut.handle(command));

        // then
        assertThat(exception).isInstanceOf(EnvironmentAlreadyExistsException.class);
        assertNoEventsHasBeenPublished();
    }

    @Test
    void should_throw_exception_when_environment_is_archived() {
        // given
        var command = fakeUpdateEnvironmentCommandBuilder()
                .build();
        environmentQueryRepositoryStub.existsByProjectIdAndNameReturns(false);
        environmentQueryRepositoryStub.getOrThrowReturns(ARCHIVED_ENVIRONMENT);
        projectQueryRepositoryStub.fetchStatusReturns(ProjectStatus.ACTIVE);

        // when
        var exception = catchException(() -> sut.handle(command));

        // then
        assertThat(exception).isInstanceOf(CannotOperateOnArchivedEnvironmentException.class);
        assertNoEventsHasBeenPublished();
    }

    @Test
    void should_throw_exception_when_project_is_archived(){
        // given
        var command = fakeUpdateEnvironmentCommandBuilder()
                .build();
        environmentQueryRepositoryStub.existsByProjectIdAndNameReturns(false);
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
