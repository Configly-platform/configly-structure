package com.configly.structure.environment.infrastructure.out.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.configly.structure.AbstractITTest;
import com.configly.structure.environment.application.port.out.EnvironmentCommandRepository;
import com.configly.structure.environment.application.port.out.EnvironmentQueryRepository;
import com.configly.structure.environment.domain.Environment;
import com.configly.structure.environment.domain.exception.EnvironmentNotFoundException;
import com.configly.structure.project.application.port.out.ProjectCommandRepository;
import com.configly.model.project.ProjectId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static com.configly.structure.builder.FakeEnvironmentBuilder.fakeEnvironmentBuilder;
import static com.configly.structure.builder.FakeProjectBuilder.fakeProjectBuilder;

class EnvironmentQueryRepositoryIT extends AbstractITTest {

    @Autowired
    private EnvironmentQueryRepository sut;

    @Autowired
    private EnvironmentCommandRepository commandRepository;

    @Autowired
    private ProjectCommandRepository projectCommandRepository;

    private Environment environment;
    private ProjectId projectId;

    @BeforeEach
    void setUp() {
        var project = fakeProjectBuilder()
                .build();
        projectCommandRepository.save(project);
        this.projectId = project.id();
        this.environment = fakeEnvironmentBuilder()
                .withProjectId(projectId)
                .build();
    }

    @Test
    void should_get_environment_by_id_and_project_id() {
        // given
        commandRepository.save(environment);

        // when
        var actual = sut.getOrThrow(environment.projectId(), environment.id());

        // then
        assertThat(actual).isEqualTo(environment);
    }

    @Test
    void should_throw_exception_when_environment_does_not_exist() {
        // given && when
        var exception = catchException(() -> sut.getOrThrow(projectId, environment.id()));

        // then
        assertThat(exception).isInstanceOf(EnvironmentNotFoundException.class);
    }

    @Test
    void should_throw_exception_when_environment_exists_but_project_id_is_wrong() {
        // given
        commandRepository.save(environment);

        // when
        var exception = catchException(() -> sut.getOrThrow(ProjectId.create(), environment.id()));

        // then
        assertThat(exception).isInstanceOf(EnvironmentNotFoundException.class);
    }

}
