package pl.feature.toggle.service.configuration;

import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.environment.infrastructure.FakeEnvironmentRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import pl.feature.toggle.service.configuration.project.infrastructure.ProjectCommandRepositoryStub;
import pl.feature.toggle.service.configuration.project.infrastructure.ProjectQueryRepositoryStub;
import pl.feature.toggle.service.contracts.shared.Metadata;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;
import pl.feature.toggle.service.outbox.FakeOutboxWriter;

import java.time.LocalDateTime;

public abstract class AbstractUnitTest {

    protected ProjectCommandRepositoryStub projectCommandRepositoryStub;
    protected ProjectQueryRepositoryStub projectQueryRepositoryStub;
    protected FakeEnvironmentRepository environmentRepository;
    protected FakeOutboxWriter outboxWriter;
    protected FakeActorProvider actorProvider;
    protected FakeCorrelationProvider correlationProvider;

    @BeforeEach
    void setUp() {
        outboxWriter = new FakeOutboxWriter();
        projectCommandRepositoryStub = new ProjectCommandRepositoryStub();
        projectQueryRepositoryStub = new ProjectQueryRepositoryStub();
        environmentRepository = new FakeEnvironmentRepository();
        actorProvider = new FakeActorProvider();
        correlationProvider = new FakeCorrelationProvider();
    }

    @AfterEach
    void tearDown() {
        projectCommandRepositoryStub.reset();
        projectQueryRepositoryStub.reset();
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
        projectCommandRepositoryStub.save(project);
    }

    protected void insertEnvironment(Environment environment) {
        environmentRepository.save(environment);
    }

    protected Metadata metadata(LocalDateTime time) {
        return new Metadata(actorProvider.current().actorId().value(), actorProvider.current().username().value(), time, correlationProvider.current().value());
    }

}
