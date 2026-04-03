package com.configly.structure.project.infrastructure.out.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.configly.structure.AbstractITTest;
import com.configly.structure.project.application.port.out.ProjectCommandRepository;
import com.configly.structure.project.application.port.out.ProjectQueryRepository;
import com.configly.structure.project.domain.Project;
import com.configly.structure.project.domain.ProjectUpdateResult;
import com.configly.structure.project.domain.exception.ProjectAlreadyExistsException;
import com.configly.structure.project.domain.exception.ProjectUpdateFailedException;
import com.configly.model.project.ProjectDescription;
import com.configly.model.project.ProjectName;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProjectCommandRepositoryIT extends AbstractITTest {

    @Autowired
    private ProjectCommandRepository sut;

    @Autowired
    private ProjectQueryRepository queryRepository;

    @Test
    void should_fail_on_duplicate_project_name_when_creating_new_project() {
        // given
        var project1 = createProject("TEST");
        sut.save(project1);
        var project2 = createProject("TEST");

        // when && then
        assertThatThrownBy(() -> sut.save(project2))
                .isInstanceOf(ProjectAlreadyExistsException.class);
    }

    @Test
    void should_save_new_project() {
        // given
        var project = createProject("TEST");

        // when
        sut.save(project);

        // then
        var actual = queryRepository.getOrThrow(project.id());
        assertThat(actual.id()).isEqualTo(project.id());
        assertThat(actual.name()).isEqualTo(project.name());
        assertThat(actual.description()).isEqualTo(project.description());
        assertThat(actual.status()).isEqualTo(project.status());
    }

    @Test
    void should_fail_on_update_when_project_not_exists() {
        // given
        var project = createProject("TEST");

        // when && then
        assertThatThrownBy(() -> sut.update(ProjectUpdateResult.noChanges(project)))
                .isInstanceOf(ProjectUpdateFailedException.class);
    }

    @Test
    void should_update_project() {
        // given
        var project = createProject("TEST");
        sut.save(project);
        var updatedProject = project.update(ProjectName.create("TEST2"), ProjectDescription.empty());

        // when
        sut.update(updatedProject);

        // then
        var expected = updatedProject.project();
        var actual = queryRepository.getOrThrow(expected.id());
        assertThat(actual.id()).isEqualTo(expected.id());
        assertThat(actual.name()).isEqualTo(expected.name());
        assertThat(actual.description()).isEqualTo(expected.description());
        assertThat(actual.status()).isEqualTo(expected.status());
        assertThat(actual.revision()).isEqualTo(expected.revision());
    }

    private Project createProject(String name) {
        return Project.create(ProjectName.create(name),
                ProjectDescription.create(UUID.randomUUID().toString()));
    }

}
