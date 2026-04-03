package com.configly.structure.environment.application.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.configly.structure.AbstractUnitTest;
import com.configly.structure.environment.application.port.in.CreateEnvironmentUseCase;
import com.configly.structure.environment.domain.EnvironmentType;
import com.configly.structure.environment.domain.exception.CannotCreateEnvironmentForMissingProjectException;
import com.configly.structure.environment.domain.exception.CannotOperateOnEnvironmentForArchivedProjectException;
import com.configly.contracts.event.environment.EnvironmentCreated;
import com.configly.model.project.ProjectStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static com.configly.structure.builder.FakeCreateEnvironmentCommandBuilder.createEnvironmentCommandBuilder;
import static com.configly.contracts.topic.KafkaTopic.CONFIGURATION;

class CreateEnvironmentHandlerTest extends AbstractUnitTest {

    private CreateEnvironmentUseCase sut;

    @BeforeEach
    void setUp() {
        sut = EnvironmentHandlerFacade.createEnvironmentUseCase(environmentCommandRepositorySpy, environmentPolicyFacade,
                outboxWriter);
    }

    @Test
    void should_create_new_environment() {
        // given
        var command = createEnvironmentCommandBuilder()
                .withProjectId(PROJECT_ID)
                .withName("TEST")
                .withType(EnvironmentType.DEV)
                .build();
        projectQueryRepositoryStub.existsReturns(true);
        projectQueryRepositoryStub.fetchStatusReturns(ProjectStatus.ACTIVE);

        // when
        sut.handle(command);

        // then
        var saved = environmentCommandRepositorySpy.getSaved();
        assertThat(saved).isNotNull();
        assertThat(saved.projectId()).isEqualTo(command.projectId());
        assertThat(saved.name()).isEqualTo(command.name());
        assertThat(saved.type()).isEqualTo(command.type());
        assertContainsEventOfType(CONFIGURATION.topicName(), EnvironmentCreated.class);
    }

    @Test
    void should_throw_exception_when_create_environment_for_missing_project() {
        // given
        var command = createEnvironmentCommandBuilder()
                .withProjectId(PROJECT_ID)
                .withName("TEST")
                .build();
        projectQueryRepositoryStub.existsReturns(false);
        environmentCommandRepositorySpy.expectNoCalls();

        // when
        var exception = catchException(() -> sut.handle(command));

        // then
        assertThat(exception).isInstanceOf(CannotCreateEnvironmentForMissingProjectException.class);
        assertNoEventsHasBeenPublished();
    }

    @Test
    void should_throw_exception_when_create_environment_for_archived_project() {
        // given
        var command = createEnvironmentCommandBuilder()
                .withProjectId(PROJECT_ID)
                .withName("TEST")
                .build();
        projectQueryRepositoryStub.fetchStatusReturns(ProjectStatus.ARCHIVED);
        environmentCommandRepositorySpy.expectNoCalls();
        projectQueryRepositoryStub.existsReturns(true);

        // when
        var exception = catchException(() -> sut.handle(command));

        // then
        assertThat(exception).isInstanceOf(CannotOperateOnEnvironmentForArchivedProjectException.class);
        assertNoEventsHasBeenPublished();
    }

}
