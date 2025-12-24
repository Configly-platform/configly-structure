package pl.feature.toggle.service.configuration;

import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.environment.infrastructure.FakeEnvironmentRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.configuration.project.infrastructure.FakeProjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import pl.feature.toggle.service.contracts.shared.Metadata;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;
import pl.feature.toggle.service.outbox.FakeOutboxWriter;

public abstract class AbstractUnitTest {

    protected FakeProjectRepository projectRepository;
    protected FakeEnvironmentRepository environmentRepository;
    protected FakeOutboxWriter outboxWriter;
    protected FakeActorProvider actorProvider;

    @BeforeEach
    void setUp() {
        outboxWriter = new FakeOutboxWriter();
        projectRepository = new FakeProjectRepository();
        environmentRepository = new FakeEnvironmentRepository();
        actorProvider = new FakeActorProvider();
    }

    @AfterEach
    void tearDown() {
        projectRepository.clear();
        outboxWriter.clear();
        environmentRepository.clear();
    }

    protected Project createProject(String name, String description) {
        return Project.create(ProjectName.create(name), ProjectDescription.create(description));
    }

    protected Environment createEnvironment(String projectId, String name) {
        return Environment.create(ProjectId.create(projectId), EnvironmentName.create(name));
    }

    protected void insertProject(Project project) {
        projectRepository.save(project);
    }

    protected void insertEnvironment(Environment environment) {
        environmentRepository.save(environment);
    }

    protected Metadata metadata() {
        return Metadata.create(actorProvider.current().actorId().value(), actorProvider.current().username().value());
    }

}
