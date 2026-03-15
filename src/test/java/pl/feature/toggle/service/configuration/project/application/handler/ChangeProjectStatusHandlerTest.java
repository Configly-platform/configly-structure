package pl.feature.toggle.service.configuration.project.application.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.feature.toggle.service.configuration.AbstractUnitTest;
import pl.feature.toggle.service.configuration.project.application.port.in.ChangeProjectStatusUseCase;
import pl.feature.toggle.service.configuration.project.application.port.out.environment.CascadedEnvironmentStatusChange;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectNotFoundException;
import pl.feature.toggle.service.contracts.event.environment.EnvironmentStatusChanged;
import pl.feature.toggle.service.contracts.event.project.ProjectStatusChanged;
import pl.feature.toggle.service.model.CreatedAt;
import pl.feature.toggle.service.model.Revision;
import pl.feature.toggle.service.model.environment.EnvironmentStatus;
import pl.feature.toggle.service.model.project.ProjectStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static pl.feature.toggle.service.configuration.builder.FakeChangeProjectStatusCommandBuilder.fakeChangeProjectStatusCommandBuilder;
import static pl.feature.toggle.service.configuration.builder.FakeProjectBuilder.fakeProjectBuilder;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.CONFIGURATION;

class ChangeProjectStatusHandlerTest extends AbstractUnitTest {

    private ChangeProjectStatusUseCase sut;

    @BeforeEach
    void setUp() {
        sut = ProjectHandlerFacade.changeProjectStatusUseCase(projectCommandRepositorySpy, projectQueryRepositoryStub,
                outboxWriter, environmentStatusCascadeSpy);
    }

    @Test
    void should_do_nothing_when_activate_project_that_is_already_active() {
        // given
        var command = fakeChangeProjectStatusCommandBuilder()
                .withProjectStatus(ProjectStatus.ACTIVE)
                .build();

        projectQueryRepositoryStub.getOrThrowReturns(ACTIVE_PROJECT);
        projectCommandRepositorySpy.expectNoCalls();
        environmentStatusCascadeSpy.expectNoCalls();


        // when && then
        assertThatCode(() -> sut.handle(command)).doesNotThrowAnyException();
        assertNoEventsHasBeenPublished();
    }

    @Test
    void should_do_nothing_when_archive_project_that_is_already_archived() {
        // given
        var command = fakeChangeProjectStatusCommandBuilder()
                .withProjectStatus(ProjectStatus.ARCHIVED)
                .build();

        projectQueryRepositoryStub.getOrThrowReturns(ARCHIVED_PROJECT);
        projectCommandRepositorySpy.expectNoCalls();
        environmentStatusCascadeSpy.expectNoCalls();

        // when && then
        assertThatCode(() -> sut.handle(command)).doesNotThrowAnyException();
        assertNoEventsHasBeenPublished();
    }

    @Test
    void should_change_project_status_to_archived_when_project_is_active() {
        // given
        var command = fakeChangeProjectStatusCommandBuilder()
                .withProjectStatus(ProjectStatus.ARCHIVED)
                .build();

        projectQueryRepositoryStub.getOrThrowReturns(ACTIVE_PROJECT);

        // when
        sut.handle(command);

        // then
        var updatedProject = projectCommandRepositorySpy.getUpdated();
        var lastArchivedProjectId = environmentStatusCascadeSpy.getLastArchived();

        assertThat(updatedProject.status()).isEqualTo(ProjectStatus.ARCHIVED);
        assertThat(lastArchivedProjectId).isEqualTo(updatedProject.id());
        assertContainsEventOfType(CONFIGURATION.topic(), ProjectStatusChanged.class);
    }

    @Test
    void should_change_project_status_to_active_when_project_is_archived() {
        // given
        var command = fakeChangeProjectStatusCommandBuilder()
                .withProjectStatus(ProjectStatus.ACTIVE)
                .build();

        projectQueryRepositoryStub.getOrThrowReturns(ARCHIVED_PROJECT);

        // when
        sut.handle(command);

        // then
        var updatedProject = projectCommandRepositorySpy.getUpdated();
        var lastArchivedProjectId = environmentStatusCascadeSpy.getLastArchived();

        assertThat(lastArchivedProjectId).isNull();
        assertThat(updatedProject.status()).isEqualTo(ProjectStatus.ACTIVE);
        assertContainsEventOfType(CONFIGURATION.topic(), ProjectStatusChanged.class);
        assertDoesNotContainEventOfType(CONFIGURATION.topic(), EnvironmentStatusChanged.class);
    }

    @Test
    void should_change_project_status_to_archive_and_emit_environment_status_changed_events_when_project_is_active(){
        // given
        var command = fakeChangeProjectStatusCommandBuilder()
                .withProjectStatus(ProjectStatus.ARCHIVED)
                .build();

        environmentStatusCascadeSpy.archiveCascadeByProjectIdReturns(List.of(
                new CascadedEnvironmentStatusChange(
                        ENVIRONMENT_ID_1,
                        PROJECT_ID,
                        EnvironmentStatus.ARCHIVED,
                        EnvironmentStatus.ACTIVE,
                        Revision.initialRevision(),
                        CreatedAt.now()
                ),
                new CascadedEnvironmentStatusChange(
                        ENVIRONMENT_ID_2,
                        PROJECT_ID,
                        EnvironmentStatus.ARCHIVED,
                        EnvironmentStatus.ACTIVE,
                        Revision.initialRevision(),
                        CreatedAt.now()
                )
        ));
        projectQueryRepositoryStub.getOrThrowReturns(ACTIVE_PROJECT);

        // when
        sut.handle(command);

        // then
        var updatedProject = projectCommandRepositorySpy.getUpdated();
        var lastArchivedProjectId = environmentStatusCascadeSpy.getLastArchived();

        assertThat(lastArchivedProjectId).isEqualTo(ACTIVE_PROJECT.id());
        assertThat(updatedProject.status()).isEqualTo(ProjectStatus.ARCHIVED);
        assertHasEventCountOfType(CONFIGURATION.topic(), ProjectStatusChanged.class, 1);
        assertHasEventCountOfType(CONFIGURATION.topic(), EnvironmentStatusChanged.class, 2);
    }

    @Test
    void should_throw_exception_when_project_not_exists() {
        // given
        projectCommandRepositorySpy.expectNoCalls();
        environmentStatusCascadeSpy.expectNoCalls();

        var command = fakeChangeProjectStatusCommandBuilder()
                .withProjectStatus(ProjectStatus.ACTIVE)
                .withProjectId(ACTIVE_PROJECT.id())
                .build();
        projectQueryRepositoryStub.getOrThrowThrows(new ProjectNotFoundException(ACTIVE_PROJECT.id()));

        // when
        var exception = catchException(() -> sut.handle(command));

        // then
        assertThat(exception).isInstanceOf(ProjectNotFoundException.class);
        assertNoEventsHasBeenPublished();
    }
}
