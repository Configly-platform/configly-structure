package com.configly.structure.environment.application.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.configly.structure.AbstractUnitTest;
import com.configly.structure.environment.application.port.in.UpdateEnvironmentUseCase;
import com.configly.structure.environment.domain.exception.CannotOperateOnArchivedEnvironmentException;
import com.configly.structure.environment.domain.exception.CannotOperateOnEnvironmentForArchivedProjectException;
import com.configly.structure.environment.domain.exception.EnvironmentNotFoundException;
import com.configly.contracts.event.environment.EnvironmentUpdated;
import com.configly.model.environment.EnvironmentName;
import com.configly.model.project.ProjectStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static com.configly.structure.builder.FakeUpdateEnvironmentCommandBuilder.fakeUpdateEnvironmentCommandBuilder;
import static com.configly.contracts.topic.KafkaTopic.CONFIGURATION;

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
        projectQueryRepositoryStub.fetchStatusReturns(ProjectStatus.ACTIVE);

        // when
        sut.handle(command);

        // then
        var updated = environmentCommandRepositorySpy.getUpdated();
        var saved = environmentCommandRepositorySpy.getSaved();
        assertThat(saved).isNull();
        assertThat(updated).isNotNull();
        assertThat(updated.name()).isEqualTo(command.name());
        assertContainsEventOfType(CONFIGURATION.topicName(), EnvironmentUpdated.class);
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
    void should_throw_exception_when_environment_is_archived() {
        // given
        var command = fakeUpdateEnvironmentCommandBuilder()
                .build();
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
