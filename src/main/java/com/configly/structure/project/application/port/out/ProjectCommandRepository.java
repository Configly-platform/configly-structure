package com.configly.structure.project.application.port.out;

import com.configly.structure.project.domain.Project;
import com.configly.structure.project.domain.ProjectUpdateResult;

public interface ProjectCommandRepository {

    void save(Project project);

    void update(ProjectUpdateResult updateResult);
}
