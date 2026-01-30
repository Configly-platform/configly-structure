package pl.feature.toggle.service.configuration.environment.infrastructure.out.db;

import pl.feature.toggle.service.configuration.AbstractITTest;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentRepository;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectCommandRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EnvironmentRepositoryIT extends AbstractITTest {

    @Autowired
    private EnvironmentRepository sut;

    @Autowired
    private ProjectCommandRepository projectRepository;

    private ProjectId projectId;

    @BeforeEach
    void setUp() {
        projectId = createProject();
    }

    @Test
    @DisplayName("Should save and load environment")
    void test01() {
        // given
        var environment = createEnvironment("TEST", projectId.uuid().toString());
        sut.save(environment);

        // when
        var result = sut.findById(environment.id()).orElseThrow();

        // then
        assertThat(result.name()).isEqualTo(environment.name());
        assertThat(result.projectId()).isEqualTo(environment.projectId());
        assertThat(result.id()).isEqualTo(environment.id());
    }

    @Test
    @DisplayName("Should save and check if environment exists")
    void test02() {
        // given
        var environment = createEnvironment("TEST", projectId.uuid().toString());
        sut.save(environment);

        // when
        var result = sut.exists(environment.id());

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when environment does not exist by id")
    void test03() {
        // given
        var environmentId = EnvironmentId.create();

        // when
        var result = sut.exists(environmentId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return false when environment does not exist by name and projectId")
    void test04() {
        // given
        var environment = createEnvironment("TEST", UUID.randomUUID().toString());

        // when
        var result = sut.existsByProjectIdAndName(environment.projectId(), environment.name());

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return true when environment exists by id")
    void test05() {
        // given
        var environment = createEnvironment("TEST", projectId.uuid().toString());
        sut.save(environment);

        // when
        var result = sut.exists(environment.id());

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return true when environment exists by name and project id")
    void test06() {
        // given
        var environment = createEnvironment("TEST", projectId.uuid().toString());
        sut.save(environment);

        // when
        var result = sut.existsByProjectIdAndName(environment.projectId(), environment.name());

        // then
        assertThat(result).isTrue();
    }

    private Environment createEnvironment(String name, String projectId) {
        return Environment.create(ProjectId.create(projectId), EnvironmentName.create(name));
    }

    private ProjectId createProject() {
        var project = Project.create(ProjectName.create("TEST"), ProjectDescription.create("TEST"));
        projectRepository.save(project);
        return project.id();
    }

}
