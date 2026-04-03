package com.configly.structure;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import com.configly.structure.builder.FakeProjectBuilder;
import com.configly.structure.environment.application.policy.EnvironmentPolicyFacade;
import com.configly.structure.environment.domain.Environment;
import com.configly.structure.environment.domain.EnvironmentType;
import com.configly.structure.environment.support.EnvironmentCommandRepositorySpy;
import com.configly.structure.environment.support.EnvironmentQueryRepositoryStub;
import com.configly.structure.project.domain.Project;
import com.configly.structure.project.support.EnvironmentStatusCascadeSpy;
import com.configly.structure.project.support.ProjectCommandRepositorySpy;
import com.configly.structure.project.support.ProjectQueryRepositoryStub;
import com.configly.contracts.shared.Metadata;
import com.configly.model.environment.EnvironmentId;
import com.configly.model.environment.EnvironmentName;
import com.configly.model.environment.EnvironmentStatus;
import com.configly.model.project.ProjectId;
import com.configly.model.project.ProjectStatus;
import com.configly.outbox.FakeOutboxWriter;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static com.configly.structure.builder.FakeEnvironmentBuilder.fakeEnvironmentBuilder;

public abstract class AbstractUnitTest {

    protected static final ProjectId PROJECT_ID = ProjectId.create();
    protected static final EnvironmentId ENVIRONMENT_ID_1 = EnvironmentId.create();
    protected static final EnvironmentId ENVIRONMENT_ID_2 = EnvironmentId.create();

    protected static final Environment ACTIVE_ENVIRONMENT = fakeEnvironmentBuilder()
            .withStatus(EnvironmentStatus.ACTIVE)
            .build();
    protected static final Environment ARCHIVED_ENVIRONMENT = fakeEnvironmentBuilder()
            .withStatus(EnvironmentStatus.ARCHIVED)
            .build();

    protected static final Project ACTIVE_PROJECT = FakeProjectBuilder
            .fakeProjectBuilder()
            .withStatus(ProjectStatus.ACTIVE)
            .build();
    protected static final Project ARCHIVED_PROJECT = FakeProjectBuilder
            .fakeProjectBuilder()
            .withStatus(ProjectStatus.ARCHIVED)
            .build();


    protected ProjectCommandRepositorySpy projectCommandRepositorySpy;
    protected ProjectQueryRepositoryStub projectQueryRepositoryStub;
    protected EnvironmentCommandRepositorySpy environmentCommandRepositorySpy;
    protected EnvironmentQueryRepositoryStub environmentQueryRepositoryStub;
    protected FakeOutboxWriter outboxWriter;
    protected FakeActorProvider actorProvider;
    protected FakeCorrelationProvider correlationProvider;
    protected EnvironmentPolicyFacade environmentPolicyFacade;
    protected EnvironmentStatusCascadeSpy environmentStatusCascadeSpy;

    @BeforeEach
    void setUp() {
        outboxWriter = new FakeOutboxWriter();
        projectCommandRepositorySpy = new ProjectCommandRepositorySpy();
        projectQueryRepositoryStub = new ProjectQueryRepositoryStub();
        environmentCommandRepositorySpy = new EnvironmentCommandRepositorySpy();
        environmentQueryRepositoryStub = new EnvironmentQueryRepositoryStub();
        actorProvider = new FakeActorProvider();
        correlationProvider = new FakeCorrelationProvider();
        environmentPolicyFacade = EnvironmentPolicyFacade.create(environmentQueryRepositoryStub, projectQueryRepositoryStub);
        environmentStatusCascadeSpy = new EnvironmentStatusCascadeSpy();
    }

    @AfterEach
    void tearDown() {
        projectCommandRepositorySpy.reset();
        projectQueryRepositoryStub.reset();
        environmentCommandRepositorySpy.reset();
        environmentQueryRepositoryStub.reset();
        environmentStatusCascadeSpy.reset();
        outboxWriter.clear();
    }

    protected Environment createEnvironment(String projectId, String name) {
        return Environment.create(ProjectId.create(projectId), EnvironmentName.create(name), EnvironmentType.TEST);
    }

    protected Metadata metadata(LocalDateTime time) {
        return new Metadata(actorProvider.current().actorId().value(), actorProvider.current().username().value(), time, correlationProvider.current().value());
    }

    protected void assertNoEventsHasBeenPublished() {
        assertThat(outboxWriter.noEventsHaveBeenPublished()).isTrue();
    }

    protected void assertContainsEventOfType(String topic, Class<?> eventClass) {
        assertThat(outboxWriter.containsEventOfType(topic, eventClass)).isTrue();
    }

    protected void assertDoesNotContainEventOfType(String topic, Class<?> eventClass) {
        assertThat(outboxWriter.containsEventOfType(topic, eventClass)).isFalse();
    }

    protected void assertHasEventCountOfType(String topic, Class<?> eventClass, int eventCount) {
        assertThat(outboxWriter.hasEventTypeCountForTopic(topic, eventClass, eventCount)).isTrue();
    }

}
