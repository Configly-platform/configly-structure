package pl.feature.toggle.service;

import com.ftaas.domain.environment.EnvironmentName;
import com.ftaas.domain.project.ProjectDescription;
import com.ftaas.domain.project.ProjectId;
import com.ftaas.domain.project.ProjectName;
import github.saqie.ftaasoutbox.FakeOutboxWriter;
import pl.feature.toggle.service.environment.domain.Environment;
import pl.feature.toggle.service.environment.infrastructure.FakeEnvironmentRepository;
import pl.feature.toggle.service.project.domain.Project;
import pl.feature.toggle.service.project.infrastructure.FakeProjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractUnitTest {

    protected FakeProjectRepository projectRepository;
    protected FakeEnvironmentRepository environmentRepository;
    protected FakeOutboxWriter outboxWriter;

    @BeforeEach
    void setUp() {
        outboxWriter = new FakeOutboxWriter();
        projectRepository = new FakeProjectRepository();
        environmentRepository = new FakeEnvironmentRepository();
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

}
