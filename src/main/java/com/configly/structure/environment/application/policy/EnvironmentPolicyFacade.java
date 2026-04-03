package com.configly.structure.environment.application.policy;

import lombok.AllArgsConstructor;
import com.configly.structure.environment.application.port.out.EnvironmentQueryRepository;
import com.configly.structure.environment.domain.Environment;
import com.configly.structure.project.application.port.out.ProjectQueryRepository;

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
