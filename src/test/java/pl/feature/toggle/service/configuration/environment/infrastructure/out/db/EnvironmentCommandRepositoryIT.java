package pl.feature.toggle.service.configuration.environment.infrastructure.out.db;

import pl.feature.toggle.service.configuration.AbstractITTest;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentCommandRepository;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentQueryRepository;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentType;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentUpdateResult;
import pl.feature.toggle.service.configuration.environment.domain.exception.EnvironmentAlreadyExistsException;
import pl.feature.toggle.service.configuration.environment.domain.exception.EnvironmentUpdateFailedException;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectCommandRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.environment.EnvironmentStatus;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static pl.feature.toggle.service.configuration.builder.FakeEnvironmentBuilder.fakeEnvironmentBuilder;

class EnvironmentCommandRepositoryIT extends AbstractITTest {

    @Autowired
    private EnvironmentCommandRepository sut;

    @Autowired
    private EnvironmentQueryRepository queryRepository;

    @Autowired
    private ProjectCommandRepository projectRepository;

    private ProjectId projectId_1;
    private ProjectId projectId_2;

    @BeforeEach
    void setUp() {
        projectId_1 = createProject();
        projectId_2 = createProject();
    }

    @Test
    void should_save_new_environment() {
        // given
        var environment = createEnvironment("TEST", projectId_1);

        // when
        sut.save(environment);

        // then
        var result = queryRepository.getOrThrow(environment.id(), environment.projectId());
        assertThat(result.name()).isEqualTo(environment.name());
        assertThat(result.projectId()).isEqualTo(environment.projectId());
        assertThat(result.id()).isEqualTo(environment.id());
    }

    @Test
    void should_update_environment() {
        // given
        var environment = createEnvironment("TEST", projectId_1);
        sut.save(environment);
        var updated = environment.changeType(EnvironmentType.PROD);

        // when
        sut.update(updated);

        // then
        var expected = updated.environment();
        var actual = queryRepository.getOrThrow(expected.id(), expected.projectId());
        assertThat(actual.name()).isEqualTo(expected.name());
        assertThat(actual.type()).isEqualTo(expected.type());
        assertThat(actual.status()).isEqualTo(expected.status());
        assertThat(actual.projectId()).isEqualTo(expected.projectId());
        assertThat(actual.id()).isEqualTo(expected.id());
        assertThat(actual.revision()).isEqualTo(expected.revision());
    }

    @Test
    void should_throw_exception_when_update_and_environment_not_exists() {
        // given
        var environment = createEnvironment("TEST", projectId_1);

        // when
        var exception = catchException(() -> sut.update(EnvironmentUpdateResult.noChanges(environment)));

        // then
        assertThat(exception).isInstanceOf(EnvironmentUpdateFailedException.class);
    }

    @Test
    void should_throw_exception_when_create_and_environment_already_exists() {
        // given
        var environment = createEnvironment("TEST", projectId_1);
        sut.save(environment);

        // when
        var exception = catchException(() -> sut.save(environment));

        // then
        assertThat(exception).isInstanceOf(EnvironmentAlreadyExistsException.class);
    }

    @Test
    void should_restore_all_environments_by_project_id() {
        // given
        var firstEnvironment = createAndSaveEnvironment("TEST_1", projectId_1, EnvironmentStatus.ARCHIVED);
        var secondEnvironment = createAndSaveEnvironment("TEST_2", projectId_1, EnvironmentStatus.ARCHIVED);
        var thirdEnvironment = createAndSaveEnvironment("TEST_2", projectId_2, EnvironmentStatus.ARCHIVED);

        // when
        sut.restoreAllByProjectId(projectId_1);

        // then
        var firstActual = queryRepository.getOrThrow(firstEnvironment.id(), firstEnvironment.projectId());
        var secondActual = queryRepository.getOrThrow(secondEnvironment.id(), secondEnvironment.projectId());
        var thirdActual = queryRepository.getOrThrow(thirdEnvironment.id(), thirdEnvironment.projectId());

        assertThat(firstActual.status()).isEqualTo(EnvironmentStatus.ACTIVE);
        assertThat(secondActual.status()).isEqualTo(EnvironmentStatus.ACTIVE);
        assertThat(thirdActual.status()).isEqualTo(EnvironmentStatus.ARCHIVED);
    }

    @Test
    void should_archive_all_environments_by_project_id() {
        // given
        var firstEnvironment = createAndSaveEnvironment("TEST_1", projectId_1, EnvironmentStatus.ACTIVE);
        var secondEnvironment = createAndSaveEnvironment("TEST_2", projectId_1, EnvironmentStatus.ACTIVE);
        var thirdEnvironment = createAndSaveEnvironment("TEST_2", projectId_2, EnvironmentStatus.ACTIVE);

        // when
        sut.archiveAllByProjectId(projectId_1);

        // then
        var firstActual = queryRepository.getOrThrow(firstEnvironment.id(), firstEnvironment.projectId());
        var secondActual = queryRepository.getOrThrow(secondEnvironment.id(), secondEnvironment.projectId());
        var thirdActual = queryRepository.getOrThrow(thirdEnvironment.id(), thirdEnvironment.projectId());

        assertThat(firstActual.status()).isEqualTo(EnvironmentStatus.ARCHIVED);
        assertThat(secondActual.status()).isEqualTo(EnvironmentStatus.ARCHIVED);
        assertThat(thirdActual.status()).isEqualTo(EnvironmentStatus.ACTIVE);
    }

    @Test
    void should_do_nothing_when_archive_environments_by_project_id_without_environments() {
        // given
        var environment = createAndSaveEnvironment("TEST_2", projectId_2, EnvironmentStatus.ACTIVE);

        // when
        assertThatCode(() -> sut.archiveAllByProjectId(projectId_1)).doesNotThrowAnyException();

        // then
        var actual = queryRepository.getOrThrow(environment.id(), environment.projectId());
        assertThat(actual.status()).isEqualTo(EnvironmentStatus.ACTIVE);
    }

    @Test
    void should_do_nothing_when_restore_environments_by_project_id_without_environments() {
        // given
        var environment = createAndSaveEnvironment("TEST_2", projectId_2, EnvironmentStatus.ARCHIVED);

        // when
        assertThatCode(() -> sut.restoreAllByProjectId(projectId_1)).doesNotThrowAnyException();

        // then
        var actual = queryRepository.getOrThrow(environment.id(), environment.projectId());
        assertThat(actual.status()).isEqualTo(EnvironmentStatus.ARCHIVED);
    }


    private Environment createEnvironment(String name, ProjectId projectId) {
        return Environment.create(projectId, EnvironmentName.create(name), EnvironmentType.DEV);
    }

    private Environment createAndSaveEnvironment(String name, ProjectId projectId, EnvironmentStatus status) {
        var environment = fakeEnvironmentBuilder()
                .withProjectId(projectId)
                .withStatus(status)
                .withName(name)
                .build();
        sut.save(environment);
        return environment;
    }

    private ProjectId createProject() {
        var project = Project.create(ProjectName.create(UUID.randomUUID().toString()), ProjectDescription.create("TEST"));
        projectRepository.save(project);
        return project.id();
    }

}
