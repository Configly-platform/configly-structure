package github.saqie.projects.environment.application.port.in;

import com.ftaas.domain.environment.EnvironmentId;

public interface CreateEnvironmentUseCase {

    EnvironmentId handle(final CreateEnvironmentCommand command);

}
