package pl.feature.toggle.service.configuration.project.application.policy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;

@Configuration
class ProjectPolicyConfiguration {

    @Bean
    ProjectPolicyFacade projectPolicyFacade(
        ProjectNameUniquePolicy projectNameUniquePolicy
    ) {
        return new ProjectPolicyFacade();
    }

    @Bean
    ProjectNameUniquePolicy projectNameUniquePolicy(ProjectQueryRepository projectQueryRepository) {
        return new ProjectNameUniquePolicy(projectQueryRepository);
    }
}
