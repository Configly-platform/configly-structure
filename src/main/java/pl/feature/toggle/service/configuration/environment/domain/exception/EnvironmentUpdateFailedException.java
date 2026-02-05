package pl.feature.toggle.service.configuration.environment.domain.exception;

import pl.feature.toggle.service.configuration.environment.domain.EnvironmentUpdateResult;

public class EnvironmentUpdateFailedException extends RuntimeException {
    public EnvironmentUpdateFailedException(EnvironmentUpdateResult updateResult) {
        super(String.format("Environment update failed for id: [%s] current revision: [%s] expected revision: [%s]",
                updateResult.environment().id().uuid(),
                updateResult.environment().revision().value(),
                updateResult.expectedRevision().value()));
    }
}
