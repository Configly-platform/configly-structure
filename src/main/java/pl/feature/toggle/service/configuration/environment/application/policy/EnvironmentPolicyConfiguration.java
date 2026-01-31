package pl.feature.toggle.service.configuration.environment.application.policy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentQueryRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;

@Configuration
class EnvironmentPolicyConfiguration {

    @Bean
    EnvironmentPolicyFacade environmentPolicyFacade(
            ProjectMustNotBeArchivedPolicy projectMustNotBeArchivedPolicy,
            UniqueEnvironmentNamePolicy uniqueEnvironmentNamePolicy,
            ProjectMustExistPolicy projectMustExistPolicy
    ) {
        return new EnvironmentPolicyFacade(uniqueEnvironmentNamePolicy, projectMustNotBeArchivedPolicy, projectMustExistPolicy);
    }

    @Bean
    ProjectMustNotBeArchivedPolicy projectMustAllowEnvironmentCreationPolicy(ProjectQueryRepository projectQueryRepository) {
        return new ProjectMustNotBeArchivedPolicy(projectQueryRepository);
    }

    @Bean
    UniqueEnvironmentNamePolicy uniqueEnvironmentNamePolicy(EnvironmentQueryRepository environmentQueryRepository) {
        return new UniqueEnvironmentNamePolicy(environmentQueryRepository);
    }

    @Bean
    ProjectMustExistPolicy projectMustExistPolicy(ProjectQueryRepository projectQueryRepository) {
        return new ProjectMustExistPolicy(projectQueryRepository);
    }

}
