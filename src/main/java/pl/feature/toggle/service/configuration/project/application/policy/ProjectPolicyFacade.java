package pl.feature.toggle.service.configuration.project.application.policy;

import lombok.AllArgsConstructor;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.model.project.ProjectName;

@AllArgsConstructor
public class ProjectPolicyFacade {

    private final ProjectNameUniquePolicy projectNameUniquePolicy;

    public static ProjectPolicyFacade create(ProjectQueryRepository projectQueryRepository) {
        return new ProjectPolicyFacade(new ProjectNameUniquePolicy(projectQueryRepository));
    }

    public void ensureCreateAllowed(ProjectName projectName) {
        projectNameUniquePolicy.ensure(projectName);
    }

    public void ensureUpdateAllowed(ProjectName projectName) {
        projectNameUniquePolicy.ensure(projectName);
    }

}
