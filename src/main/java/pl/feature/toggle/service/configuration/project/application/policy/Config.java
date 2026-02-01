package pl.feature.toggle.service.configuration.project.application.policy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;

@Configuration("projectPolicyConfig")
class Config {

    @Bean
    ProjectPolicyFacade projectPolicyFacade(
            ProjectQueryRepository projectQueryRepository
    ) {
        return ProjectPolicyFacade.create(projectQueryRepository);
    }

}
