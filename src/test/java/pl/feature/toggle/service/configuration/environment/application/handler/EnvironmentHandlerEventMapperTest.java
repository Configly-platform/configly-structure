package pl.feature.toggle.service.configuration.environment.application.handler;

import pl.feature.toggle.service.configuration.AbstractUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EnvironmentHandlerEventMapperTest extends AbstractUnitTest {

    @Test
    @DisplayName("Should map Environment to EnvironmentCreated event")
    void test01() {
        // given
        var projectId = UUID.randomUUID().toString();
        var environment = createEnvironment(projectId, "TEST");

        // when
        var result = EnvironmentHandlerEventMapper.createEnvironmentCreatedEvent(environment);

        // then
        assertThat(result.environmentId()).isEqualTo(environment.id().uuid());
        assertThat(result.projectId()).isEqualTo(UUID.fromString(projectId));
        assertThat(result.environmentName()).isEqualTo(environment.name().value());
        assertThat(result.eventId()).isNotNull();
    }

}