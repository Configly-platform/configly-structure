package pl.feature.toggle.service.configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import pl.feature.toggle.service.configuration.builder.FakeProjectBuilder;
import pl.feature.toggle.service.configuration.environment.application.policy.EnvironmentPolicyFacade;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentType;
import pl.feature.toggle.service.configuration.environment.support.EnvironmentCommandRepositorySpy;
import pl.feature.toggle.service.configuration.environment.support.EnvironmentQueryRepositoryStub;
import pl.feature.toggle.service.configuration.project.application.policy.ProjectPolicyFacade;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.configuration.project.support.EnvironmentStatusCascadeSpy;
import pl.feature.toggle.service.configuration.project.support.ProjectCommandRepositorySpy;
import pl.feature.toggle.service.configuration.project.support.ProjectQueryRepositoryStub;
import pl.feature.toggle.service.contracts.shared.Metadata;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.environment.EnvironmentStatus;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectStatus;
import pl.feature.toggle.service.outbox.FakeOutboxWriter;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.feature.toggle.service.configuration.builder.FakeEnvironmentBuilder.fakeEnvironmentBuilder;

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
    protected ProjectPolicyFacade projectPolicyFacade;
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
        projectPolicyFacade = ProjectPolicyFacade.create(projectQueryRepositoryStub);
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
