package com.configly.structure.project.application.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.configly.structure.AbstractUnitTest;
import com.configly.structure.project.application.port.in.CreateProjectUseCase;
import com.configly.contracts.event.project.ProjectCreated;

import static org.assertj.core.api.Assertions.assertThat;
import static com.configly.structure.builder.FakeCreateProjectCommandBuilder.createProjectCommandBuilder;
import static com.configly.contracts.topic.KafkaTopic.CONFIGURATION;

class CreateProjectHandlerTest extends AbstractUnitTest {

    private CreateProjectUseCase sut;

    @BeforeEach
    void setUp() {
        sut = ProjectHandlerFacade.createProjectUseCase(projectCommandRepositorySpy, outboxWriter);
    }

    @Test
    void should_create_project() {
        // given
        var command = createProjectCommandBuilder()
                .withName("TEST")
                .withDescription("TEST")
                .build();

        // when
        var result = sut.handle(command);

        // then
        assertThat(result).isNotNull();
        var saved = projectCommandRepositorySpy.getSaved();
        var updated = projectCommandRepositorySpy.getUpdated();
        assertThat(updated).isNull();
        assertThat(saved).isNotNull();
        assertThat(saved.name()).isEqualTo(command.name());
        assertThat(saved.description()).isEqualTo(command.description());
        assertContainsEventOfType(CONFIGURATION.topicName(), ProjectCreated.class);
    }

}
