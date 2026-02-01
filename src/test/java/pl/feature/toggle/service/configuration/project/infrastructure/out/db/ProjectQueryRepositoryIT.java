package pl.feature.toggle.service.configuration.project.infrastructure.out.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.feature.toggle.service.configuration.AbstractITTest;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectCommandRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectQueryRepositoryIT extends AbstractITTest {

    @Autowired
    private ProjectCommandRepository commandRepository;

    @Autowired
    private ProjectQueryRepository sut;

    @Test
    void should_get_project_by_id() {
        // given
        var project = createProject();

        // when
        var loaded = sut.getOrThrow(project.id());

        // then
        assertThat(project.name()).isEqualTo(loaded.name());
        assertThat(project.description()).isEqualTo(loaded.description());
        assertThat(project.id()).isEqualTo(loaded.id());
    }

    @Test
    void should_return_true_when_project_exists() {
        // given
        var project = createProject();

        // when
        var exists = sut.exists(project.id());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void should_return_false_when_project_does_not_exist() {
        // given
        var randomId = ProjectId.create();

        // when
        var exists = sut.exists(randomId);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    void should_return_true_when_project_exists_by_name() {
        // given
        var project = createProject();

        // when
        var exists = sut.existsByName(project.name());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void should_return_false_when_project_does_not_exist_by_name() {
        // given
        var projectName = ProjectName.create("TEST");

        // when
        var exists = sut.existsByName(projectName);

        // then
        assertThat(exists).isFalse();
    }


    private Project createProject() {
        var project = Project.create(ProjectName.create(UUID.randomUUID().toString()),
                ProjectDescription.create(UUID.randomUUID().toString()));
        commandRepository.save(project);
        return project;
    }


}