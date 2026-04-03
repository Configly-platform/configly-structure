package com.configly.structure.project.application.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.configly.structure.AbstractUnitTest;
import com.configly.structure.project.application.port.in.UpdateProjectUseCase;
import com.configly.structure.project.domain.exception.CannotOperateOnArchivedProjectException;
import com.configly.structure.project.domain.exception.ProjectNotFoundException;
import com.configly.contracts.event.project.ProjectUpdated;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static com.configly.structure.builder.FakeUpdateProjectCommandBuilder.fakeUpdateProjectCommandBuilder;
import static com.configly.contracts.topic.KafkaTopic.CONFIGURATION;

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
        assertContainsEventOfType(CONFIGURATION.topicName(), ProjectUpdated.class);
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
