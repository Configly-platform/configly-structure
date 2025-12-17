package github.saqie.projects.project.application.handler;

import com.ftaas.contracts.event.projects.ProjectCreated;
import github.saqie.projects.AbstractUnitTest;
import github.saqie.projects.project.domain.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProjectHandlerEventMapperTest extends AbstractUnitTest {

    @Test
    @DisplayName("Should map Project to ProjectCreated event")
    void test01() {
        // given
        var project = createProject("TEST", "TEST");

        // when
        var result = ProjectHandlerEventMapper.createProjectCreatedEvent(project);

        // then
        assertThat(result.projectId()).isEqualTo(project.id().uuid());
        assertThat(result.projectName()).isEqualTo(project.name().value());
        assertThat(result.eventId()).isNotNull();
    }
}