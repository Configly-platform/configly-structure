package pl.feature.toggle.service.configuration.project.application.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.feature.toggle.service.configuration.AbstractUnitTest;
import pl.feature.toggle.service.configuration.project.application.port.in.UpdateProjectUseCase;
import pl.feature.toggle.service.configuration.project.domain.exception.CannotOperateOnArchivedProjectException;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectNotFoundException;
import pl.feature.toggle.service.contracts.event.project.ProjectUpdated;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static pl.feature.toggle.service.configuration.builder.FakeUpdateProjectCommandBuilder.fakeUpdateProjectCommandBuilder;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.CONFIGURATION;

class UpdateProjectHandlerTest extends AbstractUnitTest {

    private UpdateProjectUseCase sut;

    @BeforeEach
    void setUp() {
        sut = ProjectHandlerFacade.updateProjectUseCase(projectCommandRepositorySpy, projectQueryRepositoryStub, outboxWriter);
    }

    @Test
    void should_update_project() {
        // given
        var command = fakeUpdateProjectCommandBuilder()
                .withProjectId(PROJECT_ID)
                .withName("UpdatedName")
                .withDescription("UpdatedDescription")
                .build();
        projectQueryRepositoryStub.getOrThrowReturns(ACTIVE_PROJECT);

        // when
        sut.handle(command);

        // then
        var updated = projectCommandRepositorySpy.getUpdated();
        var saved = projectCommandRepositorySpy.getSaved();
        assertThat(saved).isNull();

        assertThat(updated).isNotNull();
        assertThat(updated.name()).isEqualTo(command.name());
        assertThat(updated.description()).isEqualTo(command.description());
        assertContainsEventOfType(CONFIGURATION.topic(), ProjectUpdated.class);
    }

    @Test
    void should_throw_exception_when_project_not_exists() {
        // given
        projectCommandRepositorySpy.expectNoCalls();
        var command = fakeUpdateProjectCommandBuilder()
                .withProjectId(PROJECT_ID)
                .build();
        projectQueryRepositoryStub.getOrThrowThrows(new ProjectNotFoundException(command.projectId()));

        // when
        var exception = catchException(() -> sut.handle(command));

        // then
        var updated = projectCommandRepositorySpy.getUpdated();
        var saved = projectCommandRepositorySpy.getSaved();
        assertThat(updated).isNull();
        assertThat(saved).isNull();

        assertThat(exception).isInstanceOf(ProjectNotFoundException.class);
        assertNoEventsHasBeenPublished();
    }


    @Test
    void should_throw_exception_when_project_is_archived() {
        // given
        var command = fakeUpdateProjectCommandBuilder()
                .withProjectId(PROJECT_ID)
                .withName("UpdatedName")
                .withDescription("UpdatedDescription")
                .build();
        projectQueryRepositoryStub.getOrThrowReturns(ARCHIVED_PROJECT);
        projectCommandRepositorySpy.expectNoCalls();

        // when
        var exception = catchException(() -> sut.handle(command));

        // then
        assertThat(exception).isInstanceOf(CannotOperateOnArchivedProjectException.class);
        assertNoEventsHasBeenPublished();
    }

}
