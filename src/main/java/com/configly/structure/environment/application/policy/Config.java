package com.configly.structure.environment.application.policy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.configly.structure.environment.application.port.out.EnvironmentQueryRepository;
import com.configly.structure.project.application.port.out.ProjectQueryRepository;

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
