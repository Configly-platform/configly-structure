package com.configly.structure.environment.application.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.configly.structure.AbstractUnitTest;
import com.configly.structure.environment.application.port.in.ChangeEnvironmentTypeUseCase;
import com.configly.structure.environment.domain.EnvironmentType;
import com.configly.structure.environment.domain.exception.CannotOperateOnArchivedEnvironmentException;
import com.configly.structure.environment.domain.exception.CannotOperateOnEnvironmentForArchivedProjectException;
import com.configly.structure.environment.domain.exception.EnvironmentNotFoundException;
import com.configly.contracts.event.environment.EnvironmentTypeChanged;
import com.configly.model.project.ProjectStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static com.configly.structure.builder.FakeChangeEnvironmentTypeCommandBuilder.fakeChangeEnvironmentTypeCommandBuilder;
import static com.configly.contracts.topic.KafkaTopic.CONFIGURATION;

class ChangeEnvironmentTypeHandlerTest extends AbstractUnitTest {

    private ChangeEnvironmentTypeUseCase sut;

    @BeforeEach
    void setUp() {
        sut = EnvironmentHandlerFacade.changeEnvironmentTypeUseCase(environmentCommandRepositorySpy, environmentQueryRepositoryStub,
                environmentPolicyFacade, outboxWriter);
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
        assertContainsEventOfType(CONFIGURATION.topicName(), EnvironmentTypeChanged.class);
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
