package github.saqie.projects.project.application.handler;

import com.ftaas.contracts.event.projects.ProjectCreated;
import github.saqie.projects.AbstractUnitTest;
import github.saqie.projects.project.application.port.in.CreateProjectUseCase;
import github.saqie.projects.project.domain.exception.ProjectAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.ftaas.contracts.topic.KafkaTopic.PROJECT_ENV;
import static github.saqie.projects.builder.FakeCreateProjectCommandBuilder.createProjectCommandBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

class CreateProjectHandlerTest extends AbstractUnitTest {

    private CreateProjectUseCase sut;

    @BeforeEach
    void setUp() {
        sut = ProjectHandlerFacade.createProjectUseCase(projectRepository, outboxWriter);
    }

    @Test
    @DisplayName("Should create a new project")
    void test01() {
        // given
        var command = createProjectCommandBuilder()
                .withName("TEST")
                .withDescription("TEST")
                .build();

        // when
        var result = sut.handle(command);

        // then
        assertThat(result).isNotNull();
        assertThat(projectRepository.exists(result)).isTrue();
        assertContainsProjectCreatedEvent();
    }

    @Test
    @DisplayName("Should not create a new project when project with given name already exists")
    void test02() {
        // given
        var project = createProject("TEST", "TEST");
        projectRepository.save(project);

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
