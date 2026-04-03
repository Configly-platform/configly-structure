package com.configly.structure.project.infrastructure.out.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.configly.structure.AbstractITTest;
import com.configly.structure.project.application.port.out.ProjectCommandRepository;
import com.configly.structure.project.application.port.out.ProjectQueryRepository;
import com.configly.structure.project.domain.Project;
import com.configly.model.project.ProjectDescription;
import com.configly.model.project.ProjectId;
import com.configly.model.project.ProjectName;

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

    private Project createProject() {
        var project = Project.create(ProjectName.create(UUID.randomUUID().toString()),
                ProjectDescription.create(UUID.randomUUID().toString()));
        commandRepository.save(project);
        return project;
    }


}