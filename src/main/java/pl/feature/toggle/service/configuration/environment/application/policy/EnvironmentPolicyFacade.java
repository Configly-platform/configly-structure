package pl.feature.toggle.service.configuration.environment.application.policy;

import lombok.AllArgsConstructor;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.model.environment.EnvironmentName;

@AllArgsConstructor
public class EnvironmentPolicyFacade {

    private final UniqueEnvironmentNamePolicy uniqueEnvironmentNamePolicy;
    private final ProjectMustNotBeArchivedPolicy projectMustNotBeArchivedPolicy;
    private final ProjectMustExistPolicy projectMustExistPolicy;

    public void ensureCreateAllowed(Environment environment) {
        projectMustExistPolicy.ensure(environment.projectId());
        projectMustNotBeArchivedPolicy.ensure(environment.projectId());
        uniqueEnvironmentNamePolicy.ensure(environment.projectId(), environment.name());
    }

    public void ensureChangeStatusAllowed(Environment environment) {
        projectMustNotBeArchivedPolicy.ensure(environment.projectId());
    }

    public void ensureChangeTypeAllowed(Environment environment) {
        projectMustNotBeArchivedPolicy.ensure(environment.projectId());
    }

    public void ensureUpdateAllowed(Environment environment, EnvironmentName environmentName) {
        uniqueEnvironmentNamePolicy.ensure(environment.projectId(), environmentName);
    }

}
