package pl.feature.toggle.service.configuration.environment.application.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.feature.toggle.service.configuration.AbstractUnitTest;
import pl.feature.toggle.service.configuration.environment.application.port.in.CreateEnvironmentUseCase;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentType;
import pl.feature.toggle.service.configuration.environment.domain.exception.CannotCreateEnvironmentForMissingProjectException;
import pl.feature.toggle.service.configuration.environment.domain.exception.CannotOperateOnEnvironmentForArchivedProjectException;
import pl.feature.toggle.service.configuration.environment.domain.exception.EnvironmentAlreadyExistsException;
import pl.feature.toggle.service.contracts.event.environment.EnvironmentCreated;
import pl.feature.toggle.service.model.project.ProjectStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static pl.feature.toggle.service.configuration.builder.FakeCreateEnvironmentCommandBuilder.createEnvironmentCommandBuilder;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.CONFIGURATION;

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
