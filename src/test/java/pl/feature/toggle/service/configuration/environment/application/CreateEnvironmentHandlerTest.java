package pl.feature.toggle.service.configuration.environment.application;

import com.ftaas.contracts.event.projects.EnvironmentCreated;
import pl.feature.toggle.service.configuration.AbstractUnitTest;
import pl.feature.toggle.service.configuration.environment.application.handler.EnvironmentHandlerFacade;
import pl.feature.toggle.service.configuration.environment.application.port.in.CreateEnvironmentUseCase;
import pl.feature.toggle.service.configuration.environment.domain.exception.CannotCreateEnvironmentForMissingProject;
import pl.feature.toggle.service.configuration.environment.domain.exception.EnvironmentAlreadyExistsException;
import pl.feature.toggle.service.configuration.project.domain.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.ftaas.contracts.topic.KafkaTopic.PROJECT_ENV;
import static pl.feature.toggle.service.configuration.builder.FakeCreateEnvironmentCommandBuilder.createEnvironmentCommandBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

class CreateEnvironmentHandlerTest extends AbstractUnitTest {

    private CreateEnvironmentUseCase sut;
    private Project project;

    @BeforeEach
    void setUp() {
        sut = EnvironmentHandlerFacade.createEnvironmentUseCase(environmentRepository, projectRepository, outboxWriter);
        project = createProject("TEST", "TEST");
        insertProject(project);
    }

    @Test
    @DisplayName("Should create new environment")
    void test01() {
        // given
        var environmentCommand = createEnvironmentCommandBuilder()
                .withName("TEST")
                .withProjectId(project.id())
                .build();

        // when
        var result = sut.handle(environmentCommand);

        // then
        assertThat(result).isNotNull();
        assertThat(environmentRepository.exists(result)).isTrue();
        assertContainsEnvironmentCreatedEvent();
    }

    @Test
    @DisplayName("Should not create new environment when project not exists")
    void test02() {
        // given
        var environmentCommand = createEnvironmentCommandBuilder()
                .withName("TEST")
                .withProjectId(UUID.randomUUID())
                .build();

        // when
        var exception = catchException(() -> sut.handle(environmentCommand));

        // then
        assertThat(exception).isInstanceOf(CannotCreateEnvironmentForMissingProject.class);
        assertDoesNotContainEnvironmentCreatedEvent();
    }

    @Test
    @DisplayName("Should not create new environment when environment with given name already exists for given project")
    void test03() {
        // given
        var environment = createEnvironment(project.id().uuid().toString(), "TEST");
        insertEnvironment(environment);
        var environmentCommand = createEnvironmentCommandBuilder()
                .withName("TEST")
                .withProjectId(project.id().uuid())
                .build();

        // when
        var exception = catchException(() -> sut.handle(environmentCommand));

        // then
        assertThat(exception).isInstanceOf(EnvironmentAlreadyExistsException.class);
        assertDoesNotContainEnvironmentCreatedEvent();
    }

    private void assertContainsEnvironmentCreatedEvent() {
        assertThat(outboxWriter.containsEventOfType(PROJECT_ENV.topic(), EnvironmentCreated.class)).isTrue();
    }

    private void assertDoesNotContainEnvironmentCreatedEvent() {
        assertThat(outboxWriter.containsEventOfType(PROJECT_ENV.topic(), EnvironmentCreated.class)).isFalse();
    }

}
