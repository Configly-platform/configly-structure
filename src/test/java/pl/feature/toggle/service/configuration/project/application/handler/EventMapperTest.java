package pl.feature.toggle.service.configuration.project.application.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.feature.toggle.service.configuration.AbstractUnitTest;

import static org.assertj.core.api.Assertions.assertThat;

class EventMapperTest extends AbstractUnitTest {

    @Test
    @DisplayName("Should map Project to ProjectCreated event")
    void test01() {
        // given
        var project = createProject("TEST", "TEST");

        // when
        var result = EventMapper.createProjectCreatedEvent(project, actorProvider.current(), correlationProvider.current());

        // then
        assertThat(result.projectId()).isEqualTo(project.id().uuid());
        assertThat(result.projectName()).isEqualTo(project.name().value());
        assertThat(result.eventId()).isNotNull();
        assertThat(result.metadata()).isEqualTo(metadata(result.metadata().occurredAt()));
    }


}