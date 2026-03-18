package pl.feature.toggle.service.configuration.environment.application.policy;

import lombok.AllArgsConstructor;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentQueryRepository;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.model.environment.EnvironmentName;

@AllArgsConstructor
public class EnvironmentPolicyFacade {

    private final ProjectMustNotBeArchivedPolicy projectMustNotBeArchivedPolicy;
    private final ProjectMustExistPolicy projectMustExistPolicy;

    public static EnvironmentPolicyFacade create(
            EnvironmentQueryRepository environmentQueryRepository,
            ProjectQueryRepository projectQueryRepository
    ) {
        var projectMustNotBeArchivedPolicy = ProjectMustNotBeArchivedPolicy.create(projectQueryRepository);
        var projectMustExistPolicy = ProjectMustExistPolicy.create(projectQueryRepository);
        return new EnvironmentPolicyFacade(projectMustNotBeArchivedPolicy, projectMustExistPolicy);
    }

    public void ensureCreateAllowed(Environment environment) {
        projectMustExistPolicy.ensure(environment.projectId());
        projectMustNotBeArchivedPolicy.ensure(environment.projectId());
    }

    public void ensureChangeStatusAllowed(Environment environment) {
        projectMustNotBeArchivedPolicy.ensure(environment.projectId());
    }

    public void ensureChangeTypeAllowed(Environment environment) {
        projectMustNotBeArchivedPolicy.ensure(environment.projectId());
    }

    public void ensureUpdateAllowed(Environment environment) {
        projectMustNotBeArchivedPolicy.ensure(environment.projectId());
    }

}
