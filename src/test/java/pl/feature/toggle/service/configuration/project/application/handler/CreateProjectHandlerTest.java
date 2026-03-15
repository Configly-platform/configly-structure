package pl.feature.toggle.service.configuration.project.application.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.feature.toggle.service.configuration.AbstractUnitTest;
import pl.feature.toggle.service.configuration.project.application.port.in.CreateProjectUseCase;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectAlreadyExistsException;
import pl.feature.toggle.service.contracts.event.project.ProjectCreated;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static pl.feature.toggle.service.configuration.builder.FakeCreateProjectCommandBuilder.createProjectCommandBuilder;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.CONFIGURATION;

class CreateProjectHandlerTest extends AbstractUnitTest {

    private CreateProjectUseCase sut;

    @BeforeEach
    void setUp() {
        sut = ProjectHandlerFacade.createProjectUseCase(projectCommandRepositorySpy,
                projectPolicyFacade, outboxWriter);
    }

    @Test
    void should_create_project() {
        // given
        var command = createProjectCommandBuilder()
                .withName("TEST")
                .withDescription("TEST")
                .build();
        projectQueryRepositoryStub.existsByNameReturns(false);

        // when
        var result = sut.handle(command);

        // then
        assertThat(result).isNotNull();
        var saved = projectCommandRepositorySpy.getSaved();
        var updated = projectCommandRepositorySpy.getUpdated();
        assertThat(updated).isNull();
        assertThat(saved).isNotNull();
        assertThat(saved.name()).isEqualTo(command.name());
        assertThat(saved.description()).isEqualTo(command.description());
        assertContainsEventOfType(CONFIGURATION.topic(), ProjectCreated.class);
    }

    @Test
    void should_not_create_project_when_project_with_name_already_exists() {
        // given
        projectQueryRepositoryStub.existsByNameReturns(true);

        var command = createProjectCommandBuilder()
                .withName("TEST")
                .withDescription("TEST")
                .build();

        // when
        var exception = catchException(() -> sut.handle(command));

        // then
        assertThat(exception).isInstanceOf(ProjectAlreadyExistsException.class);
        assertNoEventsHasBeenPublished();
    }

}
