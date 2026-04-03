package com.configly.structure.environment.infrastructure;

import com.configly.structure.environment.application.handler.EnvironmentHandlerFacade;
import com.configly.structure.environment.application.policy.EnvironmentPolicyFacade;
import com.configly.structure.environment.application.port.in.ChangeEnvironmentStatusUseCase;
import com.configly.structure.environment.application.port.in.ChangeEnvironmentTypeUseCase;
import com.configly.structure.environment.application.port.in.CreateEnvironmentUseCase;
import com.configly.structure.environment.application.port.in.UpdateEnvironmentUseCase;
import com.configly.structure.environment.application.port.out.EnvironmentCommandRepository;
import com.configly.structure.environment.application.port.out.EnvironmentQueryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.configly.outbox.api.OutboxWriter;

@Configuration("environmentApplicationConfig")
class Config {

    @Bean
    CreateEnvironmentUseCase createEnvironmentUseCase(
            EnvironmentCommandRepository environmentCommandRepository,
            EnvironmentPolicyFacade environmentPolicyFacade,
            OutboxWriter outboxWriter
    ) {
        return EnvironmentHandlerFacade.createEnvironmentUseCase(environmentCommandRepository, environmentPolicyFacade,
                outboxWriter);
    }

    @Bean
    UpdateEnvironmentUseCase updateEnvironmentUseCase(
            EnvironmentCommandRepository environmentCommandRepository,
            EnvironmentQueryRepository environmentQueryRepository,
            EnvironmentPolicyFacade environmentPolicyFacade,
            OutboxWriter outboxWriter
    ) {
        return EnvironmentHandlerFacade.updateEnvironmentUseCase(environmentCommandRepository,
                environmentQueryRepository, environmentPolicyFacade, outboxWriter);
    }

    @Bean
    ChangeEnvironmentTypeUseCase changeEnvironmentTypeUseCase(
            EnvironmentCommandRepository environmentCommandRepository,
            EnvironmentQueryRepository environmentQueryRepository,
            EnvironmentPolicyFacade environmentPolicyFacade,
            OutboxWriter outboxWriter
    ) {
        return EnvironmentHandlerFacade.changeEnvironmentTypeUseCase(environmentCommandRepository, environmentQueryRepository,
                environmentPolicyFacade, outboxWriter);
    }

    @Bean
    ChangeEnvironmentStatusUseCase changeEnvironmentStatusUseCase(
            EnvironmentCommandRepository environmentCommandRepository,
            EnvironmentQueryRepository environmentQueryRepository,
            EnvironmentPolicyFacade environmentPolicyFacade,
            OutboxWriter outboxWriter
    ) {
        return EnvironmentHandlerFacade.changeEnvironmentStatusUseCase(environmentCommandRepository, environmentQueryRepository,
                environmentPolicyFacade, outboxWriter);
    }
}
