package com.configly.structure.project.domain.exception;

import com.configly.structure.project.domain.ProjectUpdateResult;

public class ProjectUpdateFailedException extends RuntimeException {
    public ProjectUpdateFailedException(ProjectUpdateResult updateResult) {
      super(String.format("Project update failed for id: [%s] current revision: [%s] expected revision: [%s]",
              updateResult.project().id().uuid(),
              updateResult.project().revision().value(),
              updateResult.expectedRevision().value()));
    }
}
