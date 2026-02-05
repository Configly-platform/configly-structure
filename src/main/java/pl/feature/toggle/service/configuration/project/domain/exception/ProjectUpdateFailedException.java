package pl.feature.toggle.service.configuration.project.domain.exception;

import pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult;

public class ProjectUpdateFailedException extends RuntimeException {
    public ProjectUpdateFailedException(ProjectUpdateResult updateResult) {
      super(String.format("Project update failed for id: [%s] current revision: [%s] expected revision: [%s]",
              updateResult.project().id().uuid(),
              updateResult.project().revision().value(),
              updateResult.expectedRevision().value()));
    }
}
