package com.configly.structure.environment.application.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.configly.structure.AbstractUnitTest;
import com.configly.structure.environment.application.port.in.ChangeEnvironmentStatusUseCase;
import com.configly.structure.environment.domain.exception.CannotOperateOnEnvironmentForArchivedProjectException;
import com.configly.structure.environment.domain.exception.EnvironmentNotFoundException;
import com.configly.contracts.event.environment.EnvironmentStatusChanged;
import com.configly.model.environment.EnvironmentStatus;
import com.configly.model.project.ProjectStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static com.configly.structure.builder.FakeChangeEnvironmentStatusCommandBuilder.fakeChangeEnvironmentStatusCommandBuilder;
import static com.configly.contracts.topic.KafkaTopic.CONFIGURATION;

class ChangeEnvironmentStatusHandlerTest extends AbstractUnitTest {

    private ChangeEnvironmentStatusUseCase sut;

    @BeforeEach
    void setUp() {
        sut = EnvironmentHandlerFacade.changeEnvironmentStatusUseCase(environmentCommandRepositorySpy, environmentQueryRepositoryStub,
                environmentPolicyFacade, outboxWriter);
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
        assertContainsEventOfType(CONFIGURATION.topicName(), EnvironmentStatusChanged.class);
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
        assertContainsEventOfType(CONFIGURATION.topicName(), EnvironmentStatusChanged.class);
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
