package pl.feature.toggle.service.configuration.environment.application.policy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentQueryRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;

@Configuration("environmentPolicyConfig")
class Config {

    @Bean
    EnvironmentPolicyFacade environmentPolicyFacade(
            EnvironmentQueryRepository environmentQueryRepository,
            ProjectQueryRepository projectQueryRepository
    ) {
        return EnvironmentPolicyFacade.create(environmentQueryRepository, projectQueryRepository);
    }


}
