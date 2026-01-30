package pl.feature.toggle.service.configuration.project.application.handler;

import pl.feature.toggle.service.configuration.AbstractUnitTest;
import pl.feature.toggle.service.configuration.project.application.port.in.CreateProjectUseCase;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.feature.toggle.service.contracts.event.project.ProjectCreated;

import static pl.feature.toggle.service.configuration.builder.FakeCreateProjectCommandBuilder.createProjectCommandBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.PROJECT_ENV;

class CreateProjectHandlerTest extends AbstractUnitTest {

    private CreateProjectUseCase sut;

    @BeforeEach
    void setUp() {
        sut = ProjectHandlerFacade.createProjectUseCase(projectCommandRepositoryStub, projectQueryRepositoryStub,
                outboxWriter, actorProvider, correlationProvider);
    }

    @Test
    @DisplayName("Should create a new project")
    void test01() {
        // given
        var command = createProjectCommandBuilder()
                .withName("TEST")
                .withDescription("TEST")
                .build();
        projectQueryRepositoryStub.existsByName(false);

        // when
        var result = sut.handle(command);

        // then
        assertThat(result).isNotNull();
        assertThat(projectCommandRepositoryStub.getSaved()).isNotNull();
        assertContainsProjectCreatedEvent();
    }

    @Test
    @DisplayName("Should not create a new project when project with given name already exists")
    void test02() {
        // given
        projectQueryRepositoryStub.existsByName(true);

        var command = createProjectCommandBuilder()
                .withName("TEST")
                .withDescription("TEST")
                .build();

        // when
        var exception = catchException(() -> sut.handle(command));

        // then
        assertThat(exception).isInstanceOf(ProjectAlreadyExistsException.class);
        assertDoesNotContainProjectCreatedEvent();
    }

    private void assertContainsProjectCreatedEvent() {
        assertThat(outboxWriter.containsEventOfType(PROJECT_ENV.topic(), ProjectCreated.class)).isTrue();
    }

    private void assertDoesNotContainProjectCreatedEvent() {
        assertThat(outboxWriter.containsEventOfType(PROJECT_ENV.topic(), ProjectCreated.class)).isFalse();
    }
}
