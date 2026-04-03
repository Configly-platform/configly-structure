package com.configly.structure.environment.application.handler;

import com.configly.structure.environment.application.policy.EnvironmentPolicyFacade;
import com.configly.structure.environment.application.port.in.ChangeEnvironmentStatusUseCase;
import com.configly.structure.environment.application.port.in.ChangeEnvironmentTypeUseCase;
import com.configly.structure.environment.application.port.in.CreateEnvironmentUseCase;
import com.configly.structure.environment.application.port.in.UpdateEnvironmentUseCase;
import com.configly.structure.environment.application.port.out.EnvironmentCommandRepository;
import com.configly.structure.environment.application.port.out.EnvironmentQueryRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import com.configly.outbox.api.OutboxWriter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnvironmentHandlerFacade {

    public static CreateEnvironmentUseCase createEnvironmentUseCase(
            EnvironmentCommandRepository environmentCommandRepository,
            EnvironmentPolicyFacade environmentPolicyFacade,
            OutboxWriter outboxWriter
    ) {
        return new CreateEnvironmentHandler(environmentCommandRepository, environmentPolicyFacade,
                outboxWriter);
    }

    public static ChangeEnvironmentStatusUseCase changeEnvironmentStatusUseCase(
            EnvironmentCommandRepository environmentCommandRepository,
            EnvironmentQueryRepository environmentQueryRepository,
            EnvironmentPolicyFacade environmentPolicyFacade,
            OutboxWriter outboxWriter
    ) {
        return new ChangeEnvironmentStatusHandler(environmentCommandRepository, environmentQueryRepository,
                environmentPolicyFacade, outboxWriter);
    }

    public static UpdateEnvironmentUseCase updateEnvironmentUseCase(
            EnvironmentCommandRepository environmentCommandRepository,
            EnvironmentQueryRepository environmentQueryRepository,
            EnvironmentPolicyFacade environmentPolicyFacade,
            OutboxWriter outboxWriter
    ) {
        return new UpdateEnvironmentHandler(environmentCommandRepository, environmentQueryRepository,
                environmentPolicyFacade, outboxWriter);
    }

    public static ChangeEnvironmentTypeUseCase changeEnvironmentTypeUseCase(
            EnvironmentCommandRepository environmentCommandRepository,
            EnvironmentQueryRepository environmentQueryRepository,
            EnvironmentPolicyFacade environmentPolicyFacade,
            OutboxWriter outboxWriter
    ) {
        return new ChangeEnvironmentTypeHandler(environmentCommandRepository, environmentQueryRepository,
                environmentPolicyFacade, outboxWriter);
    }

}
