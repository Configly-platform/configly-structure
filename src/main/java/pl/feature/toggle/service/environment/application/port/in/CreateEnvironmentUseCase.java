package pl.feature.toggle.service.environment.application.port.in;

import com.ftaas.domain.environment.EnvironmentId;

public interface CreateEnvironmentUseCase {

    EnvironmentId handle(final CreateEnvironmentCommand command);

}
