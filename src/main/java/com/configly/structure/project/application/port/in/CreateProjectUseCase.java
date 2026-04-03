package com.configly.structure.project.application.port.in;


import com.configly.structure.project.application.port.in.command.CreateProjectCommand;
import com.configly.model.project.ProjectId;

public interface CreateProjectUseCase {

    ProjectId handle(CreateProjectCommand command);

}
