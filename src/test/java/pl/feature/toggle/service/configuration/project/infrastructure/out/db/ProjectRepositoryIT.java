package pl.feature.toggle.service.configuration.project.infrastructure.out.db;

import pl.feature.toggle.service.configuration.AbstractITTest;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectCommandRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProjectRepositoryIT extends AbstractITTest {

    @Autowired
    private ProjectCommandRepository commandRepository;

    @Autowired
    private ProjectQueryRepository queryRepository;

    @Test
    @DisplayName("Should save and load project")
    void test01() {
        // given
        var project = createProject("TEST", "TEST");
        commandRepository.save(project);

        // when
        var loaded = queryRepository.findById(project.id()).orElseThrow();

        // then
        assertThat(project.name()).isEqualTo(loaded.name());
        assertThat(project.description()).isEqualTo(loaded.description());
        assertThat(project.id()).isEqualTo(loaded.id());
    }

    @Test
    @DisplayName("Should save and check if project exists")
    void test02() {
        // given
        var project = createProject("TEST", "TEST");
        commandRepository.save(project);

        // when
        var exists = queryRepository.exists(project.id());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when project does not exist")
    void test02b() {
        var randomId = ProjectId.create();

        var exists = queryRepository.exists(randomId);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should save and check if project exists by name")
    void test03() {
        // given
        var project = createProject("TEST", "TEST");
        commandRepository.save(project);

        // when
        var exists = queryRepository.existsByName(project.name());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when project does not exists by name")
    void test03b() {
        // given
        var projectName = ProjectName.create("TEST");

        // when
        var exists = queryRepository.existsByName(projectName);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should fail on duplicate project name")
    void test04() {
        // given
        var project1 = createProject("TEST", "TEST1");
        var project2 = createProject("TEST", "TEST2");

        commandRepository.save(project1);

        // when / then
        assertThatThrownBy(() -> commandRepository.save(project2))
                .isInstanceOf(ProjectAlreadyExistsException.class);
    }

    private Project createProject(String name, String description) {
        return Project.create(ProjectName.create(name), ProjectDescription.create(description));
    }


}