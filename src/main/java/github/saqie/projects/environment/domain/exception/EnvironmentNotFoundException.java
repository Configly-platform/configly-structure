package github.saqie.projects.environment.domain.exception;

import com.ftaas.domain.environment.EnvironmentId;

public class EnvironmentNotFoundException extends RuntimeException {
    public EnvironmentNotFoundException(final EnvironmentId environmentId) {
        super(String.format("Environment with id '%s' not found", environmentId.toString()));
    }
}
